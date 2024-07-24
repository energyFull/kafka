package org.energyfull.kafka.deploy.status;

import org.energyfull.kafka.deploy.DeployStatusInfo;
import org.springframework.stereotype.Component;

@Component
public interface DeployStatusProvider {
    DeployStatusInfo getDeployStatus(String topic);
    DeployStatusInfo getDeployStatusFromGroup(String consumerGroup);
}
