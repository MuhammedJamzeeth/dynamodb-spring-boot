package com.dynamodb.main.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DynamoDBConfigurationTest {

    @Mock
    private DynamoDBConfigurationProperties dynamoDBConfigurationProperties;

    @Mock
    private DynamoDBConfigurationProperties.DynamoDB dynamoDB;

    @InjectMocks
    private DynamoDBConfiguration dynamoDBConfiguration;

    @Test
    void testLocalDynamoDBConnection() {
        // Set up
        ReflectionTestUtils.setField(dynamoDBConfiguration, "executionMode", "local");

        // Execute
        AmazonDynamoDBAsync client = dynamoDBConfiguration.amazonDynamoDB();

        // Verify
        assertNotNull(client, "DynamoDB client should not be null");
        // Verify client is configured for local use by attempting a simple operation
        try {
            client.listTables();
        } catch (Exception e) {
            // We expect a connection exception in test environment, but not a configuration error
            assertTrue(e.getMessage().contains("localhost") || e.getMessage().contains("8000"),
                    "Should attempt to connect to local endpoint");
        }
    }

    @Test
    void testDynamoDBMapperConfig() {
        // Set up
        when(dynamoDBConfigurationProperties.getDynamoDB()).thenReturn(dynamoDB);
        when(dynamoDB.getTablePrefix()).thenReturn("test_");

        // Execute
        var config = dynamoDBConfiguration.dynamoDBMapperConfig();

        // Verify
        assertNotNull(config, "DynamoDB Mapper config should not be null");
        assertEquals("test_", config.getTableNameOverride().getTableNamePrefix(),
                "Table prefix should match configured value");
    }
}
