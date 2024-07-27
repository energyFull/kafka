package org.energyfull.kafka.deploy.sample.producer;


import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


@RequiredArgsConstructor
@Component
public class TestScheduler {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private AtomicInteger sequence = new AtomicInteger(0);


    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.SECONDS)
    public void scheduledTask() {
        LocalDateTime now = LocalDateTime.now();
        int seq = sequence.incrementAndGet();
        kafkaTemplate.send("test-topic",
                String.format("message-%d published at %s", seq, now.toString()));
    }
}
