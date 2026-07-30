package com.leandro.shop.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leandro.shop.user.entity.AccountStatus;
import com.leandro.shop.user.entity.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object representing a user")
public record UserResponse(
    @Schema(description = "The unique identifier of the user", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID id,

    @Schema(description = "The first name of the user", example = "John")
    @JsonProperty("first_name")
    String firstName,

    @Schema(description = "The last name of the user", example = "Doe")
    @JsonProperty("last_name")
    String lastName,

    @Schema(description = "The email of the user", example = "john.doe@example.com")
    String email,

    @Schema(description = "The current status of the user account", example = "ACTIVE")
    AccountStatus status,

    @Schema(description = "The role assigned to the user", example = "CUSTOMER")
    UserRole role,

    @Schema(description = "The date and time when the user was created", example = "2023-10-27T10:00:00")
    @JsonProperty("created_at")
    LocalDateTime createdAt,

    @Schema(description = "The date and time when the user was last updated", example = "2023-10-27T10:00:00")
    @JsonProperty("updated_at")
    LocalDateTime updatedAt
){}
