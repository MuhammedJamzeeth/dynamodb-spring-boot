package com.dynamodb.main.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class OrganizationNotFundException extends ResponseStatusException {

    private static final String DEFAULT_MESSAGE = "Organization not found. Please verify the orgId or ensure the user exists in the system.";

    public OrganizationNotFundException() {
        super(HttpStatus.NO_CONTENT, DEFAULT_MESSAGE);
    }
}
