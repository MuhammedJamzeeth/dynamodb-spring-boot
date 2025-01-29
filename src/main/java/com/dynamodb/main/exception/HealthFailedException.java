package com.dynamodb.main.exception;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class HealthFailedException extends ResponseStatusException {

    public HealthFailedException(@NonNull String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
