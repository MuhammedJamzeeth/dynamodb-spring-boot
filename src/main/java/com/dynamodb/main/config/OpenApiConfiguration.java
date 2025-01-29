package com.dynamodb.main.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(OpenApiConfigurationProperties.class)
public class OpenApiConfiguration {

    private final OpenApiConfigurationProperties openApiConfigurationProperties;

    @Bean
    public OpenAPI customOpenAPI() {
        final var properties = openApiConfigurationProperties.getOpenApi();

        return new OpenAPI()
                .info(new Info().title(properties.getTitle()).version(properties.getApiVersion()).description(properties.getDescription()));
    }
}
