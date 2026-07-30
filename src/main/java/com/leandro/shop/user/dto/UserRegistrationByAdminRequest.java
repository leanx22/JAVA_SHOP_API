package com.leandro.shop.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leandro.shop.user.entity.AccountStatus;
import com.leandro.shop.user.entity.UserRole;
import jakarta.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request object for creating a new user by an admin")
public record UserRegistrationByAdminRequest(
        @Schema(description = "The first name of the user", example = "John", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("first_name")
        @NotBlank(message = "First name is required")
        @Size(min=2, max=75, message = "First name must be between 2 and 75 characters")
        @Pattern(
                regexp = "^[\\p{L} '-]+$",
                message = "First name contains invalid characters"
        )
        String firstName,

        @Schema(description = "The last name of the user", example = "Doe", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("last_name")
        @NotBlank(message = "Last name is required")
        @Size(min=2, max=75, message = "Last name must be between 2 and 75 characters")
        @Pattern(
                regexp = "^[\\p{L} '-]+$",
                message = "Last name contains invalid characters"
        )
        String lastName,

        @Schema(description = "The email address of the user", example = "john.admin@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 254, message = "Email is too long")
        String email,

        @Schema(description = "The password for the user", example = "securePassword123!", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Password is required")
        @Size(min=8, max=128,  message = "Password must be between 8 and 128 characters")
        String password,

        @Schema(description = "The role assigned to the user", example = "CUSTOMER", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("user_role")
        @NotNull(message = "User role is required")
        UserRole role,

        @Schema(description = "The initial account status", example = "ACTIVE", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("account_status")
        @NotNull(message = "Account status is required")
        AccountStatus status
){}
