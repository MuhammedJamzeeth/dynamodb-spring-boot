package com.dynamodb.main.controller;

import com.dynamodb.main.dto.ExceptionResponseDto;
import com.dynamodb.main.model.UserTraits;
import com.dynamodb.main.repository.UserTraitsRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organization")
@Tag(name = "Organization", description = "Endpoints for managing user traits by organization.")
public class OrganizationController {
    private final UserTraitsRepository userTraitsRepository;

    @GetMapping(value = "/{userId}/users/{orgId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get UserTraits Details By orgId ", description = "Retrieves details of user traits by user id and org id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User traits retrieve successfully"),
            @ApiResponse(responseCode = "404", description = "User traits not found. Please verify the userId or ensure the user exists in the system.",
                    content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))
            )
    })
    public ResponseEntity<UserTraits> getUserByUserIdAndOrgId(@PathVariable String userId, @PathVariable String orgId) {
        return ResponseEntity.ok(userTraitsRepository.getUserTraitsByUserIdAndOrgId(userId, orgId));
    }
}

