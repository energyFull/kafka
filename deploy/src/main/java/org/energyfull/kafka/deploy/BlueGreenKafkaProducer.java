package org.energyfull.kafka.deploy;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerGroupMetadata;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.energyfull.kafka.deploy.status.DeployStatusProvider;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

@RequiredArgsConstructor
public class BlueGreenKafkaProducer<K, V> implements Producer<K, V> {
    private final Producer<K, V> producer;
    private final DeployStatusProvider deployStatusProvider;
    private final ThreadLocal<Boolean> isTransactional = new ThreadLocal<>();
    private final ThreadLocal<Map<String, DeployStatus>> topicStatus = new ThreadLocal<>();
    private final ThreadLocal<Map<String, DeployStatus>> groupStatus = new ThreadLocal<>();


    private void cleanThreadLocal(){
        isTransactional.remove();
        topicStatus.remove();
        groupStatus.remove();
    }


    @Override
    public void initTransactions() {
        producer.initTransactions();
    }

    @Override
    public void beginTransaction() throws ProducerFencedException {
        producer.beginTransaction();
        this.cleanThreadLocal();
    }

    @Override
    public void sendOffsetsToTransaction(Map<TopicPartition, OffsetAndMetadata> map, String consumerGroupId) throws ProducerFencedException {
        producer.sendOffsetsToTransaction(
                map,
                this.getConvertedConsumerGroup(consumerGroupId));
    }

    @Override
    public void sendOffsetsToTransaction(Map<TopicPartition, OffsetAndMetadata> map, ConsumerGroupMetadata consumerGroupMetadata) throws ProducerFencedException {
        producer.sendOffsetsToTransaction(map,
                new ConsumerGroupMetadata(
                        this.getConvertedConsumerGroup(consumerGroupMetadata.groupId()),
                        consumerGroupMetadata.generationId(),
                        consumerGroupMetadata.memberId(),
                        consumerGroupMetadata.groupInstanceId()
                ));
    }

    @Override
    public void commitTransaction() throws ProducerFencedException {
        producer.commitTransaction();
        this.cleanThreadLocal();
    }

    @Override
    public void abortTransaction() throws ProducerFencedException {
        producer.abortTransaction();
        this.cleanThreadLocal();
    }

    @Override
    public Future<RecordMetadata> send(ProducerRecord<K, V> producerRecord) {
        return producer.send(
                getTopicConvertedRecord(producerRecord)
        );
    }

    @Override
    public Future<RecordMetadata> send(ProducerRecord<K, V> producerRecord, Callback callback) {
        return producer.send(
                getTopicConvertedRecord(producerRecord),
                callback
        );
    }

    @Override
    public void flush() {
        producer.flush();
    }

    @Override
    public List<PartitionInfo> partitionsFor(String topic) {
        return producer.partitionsFor(getConvertedTopic(topic));
    }

    @Override
    public Map<MetricName, ? extends Metric> metrics() {
        return producer.metrics();
    }

    @Override
    public void close() {
        producer.close();
    }

    @Override
    public void close(Duration duration) {
        producer.close(duration);
    }

    private boolean isTransactional() {
        Boolean isTransactional = this.isTransactional.get();
        return isTransactional != null && isTransactional;
    }

    private String getConvertedTopic(String topic) {
        final DeployStatus status = this.getDeployStatus(topic);
        return switch (status){
            case Blue -> topic + BlueGreenKafkaConstants.DEFAULT_BLUE_POSTFIX;
            case Green -> topic + BlueGreenKafkaConstants.DEFAULT_GREEN_POSTFIX;
            case NotFound -> topic;
        };
    }

    private String getConvertedConsumerGroup(String consumerGroup) {
        final DeployStatus status = this.getDeployStatusFromGroup(consumerGroup);
        return switch (status){
            case Blue -> consumerGroup + BlueGreenKafkaConstants.DEFAULT_BLUE_POSTFIX;
            case Green -> consumerGroup + BlueGreenKafkaConstants.DEFAULT_GREEN_POSTFIX;
            case NotFound -> consumerGroup;
        };
    }

    private DeployStatus getDeployStatus(String topic) {
        if(isTransactional()){
            final DeployStatus status;
            var topicStatusMap = topicStatus.get();
            if(topicStatusMap == null)
                topicStatusMap = new HashMap<>();
            status = topicStatusMap.computeIfAbsent(
                    topic,
                    k ->{
                        var info = deployStatusProvider.getDeployStatus(topic);
                        var groupStatusMap = groupStatus.get();
                        if(groupStatusMap == null)
                            groupStatusMap = new HashMap<>();
                        groupStatusMap.put(info.getConsumerGroup(), info.getStatus());
                        groupStatus.set(groupStatusMap);
                        return info.getStatus();
                    }
            );
            topicStatus.set(topicStatusMap);
            return status;
        } else {
            return deployStatusProvider.getDeployStatus(topic).getStatus();
        }
    }

    private DeployStatus getDeployStatusFromGroup(String consumerGroup) {
        if(isTransactional()){
            final DeployStatus status;
            var groupStatusMap = groupStatus.get();
            if(groupStatusMap == null)
                groupStatusMap = new HashMap<>();
            status = groupStatusMap.computeIfAbsent(
                    consumerGroup,
                    k -> deployStatusProvider.getDeployStatusFromGroup(consumerGroup).getStatus()
                    );
            groupStatus.set(groupStatusMap);
            return status;
        }else{
            return deployStatusProvider.getDeployStatus(consumerGroup).getStatus();
        }
    }

    private ProducerRecord<K, V> getTopicConvertedRecord(ProducerRecord<K, V> record){
        final String topic = getConvertedTopic(record.topic());
        return new ProducerRecord<>(topic, record.partition(), record.key(), record.value());
    }
}
