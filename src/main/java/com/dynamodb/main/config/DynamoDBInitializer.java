package com.dynamodb.main.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceInUseException;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.dynamodb.main.model.UserTrait;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class DynamoDBInitializer {

    private final DynamoDBMapper dynamoDBMapper;
    private final AmazonDynamoDBAsync amazonDynamoDB;

    public DynamoDBInitializer(DynamoDBMapper dynamoDBMapper, AmazonDynamoDBAsync amazonDynamoDB) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.amazonDynamoDB = amazonDynamoDB;
    }

    @PostConstruct
    public void createTablesIfNotExist() {
        CreateTableRequest tableRequest = dynamoDBMapper
                .generateCreateTableRequest(UserTrait.class)
                .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L));

        try {
            // Check if table already exists
            TableUtils.createTableIfNotExists(amazonDynamoDB, tableRequest);
            // Wait until table is active
            TableUtils.waitUntilActive(amazonDynamoDB, tableRequest.getTableName());

            log.info("DynamoDB table {} created or already exists and is active", tableRequest.getTableName());
        } catch (InterruptedException | ResourceInUseException e) {
            log.error("Error creating DynamoDB table: ", e);
        } catch (Exception e) {
            log.error("Error creating or waiting for DynamoDB table: ", e);
            throw new RuntimeException("Failed to initialize DynamoDB table", e);
        }
    }
}