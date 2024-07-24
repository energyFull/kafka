package org.energyfull.kafka.deploy.status;

import lombok.RequiredArgsConstructor;
import org.energyfull.kafka.deploy.DeployStatusInfo;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestAPIDeployStatusProvider implements DeployStatusProvider{
    private final DeployStatusRestClient client;
    @Override
    public DeployStatusInfo getDeployStatus(String topic) {
        return client.getDeployStatus(topic, null);
    }

    @Override
    public DeployStatusInfo getDeployStatusFromGroup(String consumerGroup) {
        return client.getDeployStatus(null, consumerGroup);
    }
}
