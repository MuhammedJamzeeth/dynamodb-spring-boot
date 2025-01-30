package com.dynamodb.main.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Schema(title = "UserTraitRequest", accessMode = Schema.AccessMode.WRITE_ONLY)
public class UserTraitRequest {

    @NotNull(message = "Trait key cannot be null")
    @Schema(description = "User Trait Key", example = "trait1")
    private String traitKey;

    @NotNull(message = "Trait value cannot be null")
    @Schema(description = "Trait Value", example = "value1")
    private String value;

    @NotNull(message = "Organization ID cannot be null")
    @Schema(description = "Organization ID", example = "org1")
    private String orgId;
}
