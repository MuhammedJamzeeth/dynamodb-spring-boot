package com.dynamodb.main.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.dynamodb.main.exception.NotFoundException;
import com.dynamodb.main.exception.UserNotFoundException;
import com.dynamodb.main.model.UserTrait;
import com.dynamodb.main.model.UserTraits;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserTraitsRepository {
    private final DynamoDBMapper dynamoDBMapper;

//    public UserTrait createUserTrait(UserTrait userTrait) {
//        log.info("Creating user trait for userId: {}, traitKey: {}", userTrait.getUserId(), userTrait.getTraitKey());
//        dynamoDBMapper.save(userTrait);
//        return userTrait;
//    }

//    public Optional<UserTrait> getUserTrait(String userId, String traitKey) {
//        log.info("Fetching user trait for userId: {}, traitKey: {}", userId, traitKey);
//        UserTrait userTrait = dynamoDBMapper.load(UserTrait.class, userId, traitKey);
//        return Optional.ofNullable(userTrait);
//    }

    public UserTraits getUserTraitsByUserId(String userId) {
        log.info("Fetching all traits for userId: {}", userId);

        // check user exists or not and throw exception if not
        if (doesUserExist(userId)) {
            throw new UserNotFoundException();
        }

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":userId", new AttributeValue().withS(userId));

        DynamoDBQueryExpression<UserTrait> query = new DynamoDBQueryExpression<UserTrait>()
                .withKeyConditionExpression("userId = :userId")
                .withExpressionAttributeValues(eav);


        PaginatedQueryList<UserTrait> traits = dynamoDBMapper.query(UserTrait.class, query);
        traits.loadAllResults();

        if (traits.isEmpty()) {
            log.error("Users has no traits, user must not be present");
            return null;
        }

        final var userTraits = new UserTraits(userId, traits);

        // check the user is valid or not and throw the exception if not
        if (isValidUser(userTraits)) {
            throw new NotFoundException("Users has no traits");
        }

        return userTraits;
    }

//    public UserTrait updateUserTrait(UserTrait userTrait) {
//        log.info("Updating user trait for userId: {}, traitKey: {}", userTrait.getUserId(), userTrait.getTraitKey());
//
//        // Check if trait exists
//        Optional<UserTrait> existingTrait = getUserTrait(userTrait.getUserId(), userTrait.getTraitKey());
//        if (existingTrait.isEmpty()) {
//            throw new RuntimeException("User trait not found for update");
//        }
//
//        dynamoDBMapper.save(userTrait);
//        return userTrait;
//    }

//    public void deleteUserTrait(String userId, String traitKey) {
//        log.info("Deleting user trait for userId: {}, traitKey: {}", userId, traitKey);
//
//        UserTrait userTrait = new UserTrait();
//        userTrait.setUserId(userId);
//        userTrait.setTraitKey(traitKey);
//
//        dynamoDBMapper.delete(userTrait);
//    }

    public UserTraits getUserTraitsByUserIdAndOrgId(String userId, String orgId) {
        log.info("Fetching all traits for userId: {} and orgId: {}", userId, orgId);

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":userId", new AttributeValue().withS(userId));
        eav.put(":orgId", new AttributeValue().withS(orgId));

        // check user exists or not and throw exception if not
        if (doesUserExist(userId)) {
            throw new UserNotFoundException();
        }

        DynamoDBQueryExpression<UserTrait> query = new DynamoDBQueryExpression<UserTrait>()
                .withKeyConditionExpression("userId = :userId")
                .withFilterExpression("organizationId = :orgId")
                .withExpressionAttributeValues(eav);

        // Execute the query
        PaginatedQueryList<UserTrait> traits = dynamoDBMapper.query(UserTrait.class, query);
        traits.loadAllResults();

        // Handle empty results
        if (traits.isEmpty()) {
            log.error("Users has no traits, user must not be present");
            throw new NotFoundException("Users has no traits");
        }

        // check the user is valid or not and throw the exception if not
        final var userTraits = new UserTraits(userId, traits);
        if (isValidUser(userTraits)) {
            throw new NotFoundException("Invalid user traits");
        }

        return userTraits;
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
        return userList.isEmpty();
    }

    private boolean isValidUser(UserTraits userTraits) {
        return userTraits.getTraits().stream()
                .noneMatch(trait -> trait.getTraitValue() != null
                        && !trait.getTraitValue().isEmpty()
                        && !trait.getTraitValue().contains("null"));
    }

}

