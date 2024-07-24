
package org.energyfull.kafka.deploy.sample.producer;


import org.energyfull.kafka.deploy.EnableBlueGreenKafka;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
@EnableBlueGreenKafka
public class SampleProducerApplication {
    public static void main(String[] args) {
        var context = SpringApplication.run(SampleProducerApplication.class, args);
    }
}