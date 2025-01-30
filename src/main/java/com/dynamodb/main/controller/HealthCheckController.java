package com.dynamodb.main.controller;


import com.dynamodb.main.exception.HealthFailedException;
import com.dynamodb.main.dto.HealthResponseDto;
import com.dynamodb.main.repository.DynamoDBHealthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/health")
@Tag(name = "Health Check", description = "Endpoints for checking the health of the application and dependencies.")
public class HealthCheckController {

    private final DynamoDBHealthService dynamoDBHealthService;

    @GetMapping
    @Operation(summary = "Check application health", description = "Checks the health status of DynamoDB and the service itself.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service is healthy", content = @Content(schema = @Schema(implementation = HealthResponseDto.class))),
            @ApiResponse(responseCode = "503", description = "Service is unhealthy.", content = @Content(schema = @Schema(implementation = HealthFailedException.class)))
    })
    public ResponseEntity<HealthResponseDto> healthCheck() {
        final var response = dynamoDBHealthService.checkHealth();
        return ResponseEntity.ok(response);
    }
}
