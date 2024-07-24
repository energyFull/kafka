//package org.energyfull.kafka.deploy.sample.producer;
//
//
//import org.energyfull.kafka.deploy.status.DeployStatusProvider;
//import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//
//@Configuration
//public class KafkaConfiguration {
//
//    @Bean
//    public ProducerFactory<String, String> producerFactory(KafkaProperties kafkaProperties, DeployStatusProvider deployStatusProvider) {
//        return new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties(), deployStatusProvider);
//    }
//
//    @Bean
//    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
//        return new KafkaTemplate<>(producerFactory);
//    }
//}
