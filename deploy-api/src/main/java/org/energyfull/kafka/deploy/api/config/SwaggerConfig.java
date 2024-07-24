package org.energyfull.kafka.deploy.api.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;


@OpenAPIDefinition(
        info = @Info(title = "Kafka Deployment Management API Spec",
                description = "Kafka Deployment Management API Spec",
                version = "v1"))
@Configuration
public class SwaggerConfig {

}
