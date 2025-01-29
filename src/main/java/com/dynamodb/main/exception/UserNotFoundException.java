package com.dynamodb.main.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserNotFoundException extends ResponseStatusException {

    private static final String DEFAULT_MESSAGE = "User not found. Please verify the userId or ensure the user exists in the system.";

    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, DEFAULT_MESSAGE);
    }
}
