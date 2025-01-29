package com.dynamodb.main.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HealthStatus {
    private boolean up;
    private String message;
}
