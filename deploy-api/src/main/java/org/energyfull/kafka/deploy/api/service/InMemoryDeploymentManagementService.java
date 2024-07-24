package org.energyfull.kafka.deploy.api.service;

import org.energyfull.kafka.deploy.DeployStatus;
import org.energyfull.kafka.deploy.DeployStatusInfo;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryDeploymentManagementService implements DeploymentManagementService{
    private final ConcurrentHashMap<String, String> consumerGroupMapper = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, DeployStatus> deployStatus = new ConcurrentHashMap<>();

    @Override
    public DeployStatusInfo getDeployStatusInfo(String topic, String consumerGroup) {
        if(consumerGroup == null)
            consumerGroup = consumerGroupMapper.get(topic);
        var status = deployStatus.get(consumerGroup);
        return new DeployStatusInfo(status, consumerGroup);
    }

    @Override
    public void setStatus(String consumerGroup, DeployStatus status) {
        deployStatus.put(consumerGroup, status);
    }

    @Override
    public void addTopic(String consumerGroup, String topic) {
        deployStatus.putIfAbsent(consumerGroup, DeployStatus.NotFound);
        consumerGroupMapper.put(topic, consumerGroup);
    }
}
