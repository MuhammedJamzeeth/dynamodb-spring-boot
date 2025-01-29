package com.dynamodb.main.config;


import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(DynamoDBConfigurationProperties.class)
public class DynamoDBConfiguration {

    private final DynamoDBConfigurationProperties dynamoDBConfigurationProperties;

    @Value("${execution.mode}")
    private String executionMode;

    @Bean
    public AmazonDynamoDBAsync amazonDynamoDB() {
        if (executionMode.equals("local")) {
            final String url = "http://localhost:8000";
            return AmazonDynamoDBAsyncClientBuilder.standard()
                    .withCredentials(
                            new AWSStaticCredentialsProvider(
                                    new BasicAWSCredentials("X", "X")))
                    .withClientConfiguration(
                            new ClientConfiguration().withMaxErrorRetry(0).withConnectionTimeout(100000))
                    .withEndpointConfiguration(
                            new AwsClientBuilder.EndpointConfiguration(url, Regions.US_WEST_2.getName()))
                    .build();
        } else {
            return AmazonDynamoDBAsyncClientBuilder.standard()
                    .withCredentials(amazonAWSCredentials())
                    .withRegion(dynamoDBConfigurationProperties.getDynamoDB().getRegion())
                    .build();
        }
    }

    @Bean
    public AWSCredentialsProvider amazonAWSCredentials() {
        return new DefaultAWSCredentialsProviderChain();
    }

    @Bean
    public DynamoDBMapperConfig dynamoDBMapperConfig() {
        return new DynamoDBMapperConfig.Builder()
                .withTableNameOverride(
                        DynamoDBMapperConfig.TableNameOverride.withTableNamePrefix(dynamoDBConfigurationProperties.getDynamoDB().getTablePrefix()))
                .build();
    }

    @Bean
    public DynamoDBMapper dynamoDBMapper(
            AmazonDynamoDBAsync amazonDynamoDB, DynamoDBMapperConfig config) {
        return new DynamoDBMapper(amazonDynamoDB, config);
    }
}
