package com.dynamodb.main.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(title = "HealthResponse")
public class HealthResponseDto {
    private boolean healthy;
    private String message;

    public HealthResponseDto(boolean b, String serviceIsHealthy) {
        this.healthy = b;
        this.message = serviceIsHealthy;
    }
}
