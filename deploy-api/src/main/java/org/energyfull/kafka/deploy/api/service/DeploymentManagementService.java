package org.energyfull.kafka.deploy.api.service;

import org.energyfull.kafka.deploy.DeployStatus;
import org.energyfull.kafka.deploy.DeployStatusInfo;

public interface DeploymentManagementService {
    DeployStatusInfo getDeployStatusInfo(String topic, String consumerGroup);
    void setStatus(String consumerGroup, DeployStatus status);
    void addTopic(String consumerGroup, String topic);
}
