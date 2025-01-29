package com.dynamodb.main.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "com.dynamodb.main")
public class DynamoDBConfigurationProperties {

    private DynamoDB dynamoDB = new DynamoDB();

    @Getter
    @Setter
    public static class DynamoDB {

        private String endpoint;
        private String region;
        private String accessKey;
        private String secretKey;
        private String tablePrefix;

    }
}
