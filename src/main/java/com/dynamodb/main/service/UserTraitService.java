package com.dynamodb.main.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.dynamodb.main.exception.UserNotFoundException;
import com.dynamodb.main.model.UserTrait;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserTraitService {
    private final DynamoDBMapper dynamoDBMapper;

    public UserTrait createUserTrait(UserTrait userTrait) {
        log.info("Creating user trait for userId: {}, traitKey: {}", userTrait.getUserId(), userTrait.getTraitKey());
        dynamoDBMapper.save(userTrait);
        return userTrait;
    }

    public Optional<UserTrait> getUserTrait(String userId, String traitKey) {
        log.info("Fetching user trait for userId: {}, traitKey: {}", userId, traitKey);
        UserTrait userTrait = dynamoDBMapper.load(UserTrait.class, userId, traitKey);
        return Optional.ofNullable(userTrait);
    }

    public List<UserTrait> getUserTraitsByUserId(String userId) {
        log.info("Fetching all traits for userId: {}", userId);

        // check user exists or not and throw exception if not
        if (!doesUserExist(userId)) {
            throw new UserNotFoundException();
        }

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":userId", new AttributeValue().withS(userId));

        DynamoDBQueryExpression<UserTrait> queryExpression = new DynamoDBQueryExpression<UserTrait>()
                .withKeyConditionExpression("userId = :userId")
                .withExpressionAttributeValues(eav);

        final var traits = dynamoDBMapper.query(UserTrait.class, queryExpression);

        return dynamoDBMapper.query(UserTrait.class, queryExpression);
    }

    public UserTrait updateUserTrait(UserTrait userTrait) {
        log.info("Updating user trait for userId: {}, traitKey: {}", userTrait.getUserId(), userTrait.getTraitKey());

        // Check if trait exists
        Optional<UserTrait> existingTrait = getUserTrait(userTrait.getUserId(), userTrait.getTraitKey());
        if (existingTrait.isEmpty()) {
            throw new RuntimeException("User trait not found for update");
        }

        dynamoDBMapper.save(userTrait);
        return userTrait;
    }

    public void deleteUserTrait(String userId, String traitKey) {
        log.info("Deleting user trait for userId: {}, traitKey: {}", userId, traitKey);

        UserTrait userTrait = new UserTrait();
        userTrait.setUserId(userId);
        userTrait.setTraitKey(traitKey);

        dynamoDBMapper.delete(userTrait);
    }

    private boolean doesUserExist(String userId) {
        log.info("Checking if user with userId: {} exists.", userId);

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":userId", new AttributeValue().withS(userId));

        DynamoDBQueryExpression<UserTrait> queryExpression = new DynamoDBQueryExpression<UserTrait>()
                .withKeyConditionExpression("userId = :userId")
                .withExpressionAttributeValues(eav)
                .withLimit(1);

        List<UserTrait> userList = dynamoDBMapper.query(UserTrait.class, queryExpression);
        return !userList.isEmpty();
    }

}

