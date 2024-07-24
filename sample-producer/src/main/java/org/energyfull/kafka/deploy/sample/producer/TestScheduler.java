package org.energyfull.kafka.deploy.sample.producer;


import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@RequiredArgsConstructor
@Component
public class TestScheduler {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.SECONDS)
    public void scheduledTask() {
        kafkaTemplate.send("test-topic", "test");
    }
}
