package com.dynamodb.main.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.dynamodb.main.exception.HealthFailedException;
import com.dynamodb.main.dto.HealthResponseDto;
import com.dynamodb.main.dto.HealthStatus;
import com.dynamodb.main.model.UserTrait;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DynamoDBHealthService {

    private final DynamoDBMapper dynamoDBMapper;
    private final AmazonDynamoDBAsync amazonDynamoDBAsync;

    public HealthResponseDto checkHealth() {
        HealthStatus dynamoHealth = isDynamoDBOk();

        // check the dynamoDB health
        if (!dynamoHealth.isUp()) {
            throw new HealthFailedException("DynamoDB is down: " + dynamoHealth.getMessage());
        }

//        boolean selfCheckStatus = requestToSelf();
//        if (!selfCheckStatus) {
//            throw new HealthFailedException(dynamoHealth.getMessage());
//        }

        return new HealthResponseDto(true, "Service is healthy");
    }

    // request to self to check the service health

    // check the dynamoDB health
    HealthStatus isDynamoDBOk() {
        HealthStatus healthStatus = new HealthStatus();
        String tableName = dynamoDBMapper.generateCreateTableRequest(UserTrait.class).getTableName();
        try {
            amazonDynamoDBAsync.describeTableAsync(tableName).get().getTable();
            healthStatus.setUp(true);
        } catch (Exception e) {
            healthStatus.setMessage(e.getMessage());
            log.error(e.getMessage());
        }
        return healthStatus;
    }
}
