package org.energyfull.kafka.deploy.api.controller;


import lombok.RequiredArgsConstructor;
import org.energyfull.kafka.deploy.DeployStatus;
import org.energyfull.kafka.deploy.DeployStatusInfo;
import org.energyfull.kafka.deploy.api.service.DeploymentManagementService;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/deploy")
@RestController
public class DeploymentManagementRestController {
    private final DeploymentManagementService deploymentManagementService;

    @GetMapping("/status")
    public DeployStatusInfo getDeployStatus(
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) String consumerGroup) {
        if(topic == null && consumerGroup == null)
            throw new IllegalArgumentException("topic and consumerGroup cannot be null");
        return deploymentManagementService.getDeployStatusInfo(topic, consumerGroup);
    }

    @PutMapping("/consumer/{consumerGroup}")
    public void setStatus(@RequestParam String status, @PathVariable String consumerGroup){
        var statusType = DeployStatus.valueOf(status);
        deploymentManagementService.setStatus(consumerGroup, statusType);
    }

    @PostMapping("/consumer/{consumerGroup}/topic")
    public void addTopic(@RequestBody TopicAddRequest request, @PathVariable String consumerGroup){
        deploymentManagementService.addTopic(consumerGroup, request.getTopic());
    }
}
