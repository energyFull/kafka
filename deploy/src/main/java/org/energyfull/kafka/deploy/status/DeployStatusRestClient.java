package org.energyfull.kafka.deploy.status;

import org.energyfull.kafka.deploy.DeployStatusInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "deployment-client", url = "${energyfull.kafka.deployment-management.url:localhost}")
public interface DeployStatusRestClient {
    @GetMapping("/deploy/status")
    DeployStatusInfo getDeployStatus(@RequestParam String topic, @RequestParam String consumerGroup);
}
