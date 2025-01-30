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
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organization/users/traits")
@Tag(name = "User traits", description = "Endpoints for managing user traits.")
public class UserTraitController {

    private final UserTraitsRepository userTraitsRepository;

    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get UserTraits Details", description = "Retrieves details of user traits by user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User traits retrieve successfully"),
            @ApiResponse(responseCode = "404", description = "User not found. Please verify the userId or ensure the user exists in the system.",
                    content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))
            )
    })
    public ResponseEntity<UserTraits> getUserByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(userTraitsRepository.getUserTraitsByUserId(userId));
    }
}
