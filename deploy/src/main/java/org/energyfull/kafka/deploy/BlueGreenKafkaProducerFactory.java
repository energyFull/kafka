package org.energyfull.kafka.deploy;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.Serializer;
import org.energyfull.kafka.deploy.status.DeployStatusProvider;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;

import java.util.Map;
import java.util.function.Supplier;

public class BlueGreenKafkaProducerFactory<K, V> extends DefaultKafkaProducerFactory<K, V> {
    private final DeployStatusProvider deployStatusProvider;

    public BlueGreenKafkaProducerFactory(Map<String, Object> configs, DeployStatusProvider deployStatusProvider) {
        super(configs);
        this.deployStatusProvider = deployStatusProvider;
    }

    public BlueGreenKafkaProducerFactory(Map<String, Object> configs, Serializer<K> keySerializer, Serializer<V> valueSerializer, DeployStatusProvider deployStatusProvider) {
        super(configs, keySerializer, valueSerializer);
        this.deployStatusProvider = deployStatusProvider;
    }

    public BlueGreenKafkaProducerFactory(Map<String, Object> configs, Serializer<K> keySerializer, Serializer<V> valueSerializer, boolean configureSerializers, DeployStatusProvider deployStatusProvider) {
        super(configs, keySerializer, valueSerializer, configureSerializers);
        this.deployStatusProvider = deployStatusProvider;
    }

    public BlueGreenKafkaProducerFactory(Map<String, Object> configs, Supplier<Serializer<K>> keySerializerSupplier, Supplier<Serializer<V>> valueSerializerSupplier, DeployStatusProvider deployStatusProvider) {
        super(configs, keySerializerSupplier, valueSerializerSupplier);
        this.deployStatusProvider = deployStatusProvider;
    }

    public BlueGreenKafkaProducerFactory(Map<String, Object> configs, Supplier<Serializer<K>> keySerializerSupplier, Supplier<Serializer<V>> valueSerializerSupplier, boolean configureSerializers, DeployStatusProvider deployStatusProvider) {
        super(configs, keySerializerSupplier, valueSerializerSupplier, configureSerializers);
        this.deployStatusProvider = deployStatusProvider;
    }

    @Override
    protected Producer<K, V> createRawProducer(Map<String, Object> rawConfigs) {
        var producer = super.createRawProducer(rawConfigs);
        return new BlueGreenKafkaProducer<>(producer, deployStatusProvider);
    }
}
