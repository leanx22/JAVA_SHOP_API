package com.leandro.shop.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leandro.shop.user.entity.AccountStatus;
import com.leandro.shop.user.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request object for updating a user by an admin")
public record UserUpdateByAdminRequest(
        @Schema(description = "The first name of the user", example = "Jane")
        @JsonProperty("first_name")
        @Size(min=2, max=75, message = "First name must be between 2 and 75 characters")
        @Pattern(
                regexp = "^[\\p{L} '-]+$",
                message = "First name contains invalid characters"
        )
        String firstName,

        @Schema(description = "The last name of the user", example = "Doe")
        @JsonProperty("last_name")
        @Size(min=2, max=75, message = "Last name must be between 2 and 75 characters")
        @Pattern(
                regexp = "^[\\p{L} '-]+$",
                message = "Last name contains invalid characters"
        )
        String lastName,

        @Schema(description = "The email address of the user", example = "jane.admin@example.com")
        @Email(message = "Email must be valid")
        @Size(max = 254, message = "Email is too long")
        String email,

        @Schema(description = "The new account status", example = "SUSPENDED")
        @JsonProperty("account_status")
        AccountStatus status,

        @Schema(description = "The new role assigned to the user", example = "ADMIN")
        UserRole role,

        @Schema(description = "The new password for the user", example = "newSecurePassword123!")
        @Size(min=8, max=128,  message = "Password must be between 8 and 128 characters")
        String password
){}
