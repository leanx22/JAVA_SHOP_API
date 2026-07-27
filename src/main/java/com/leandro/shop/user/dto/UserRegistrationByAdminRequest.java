package com.leandro.shop.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leandro.shop.user.entity.AccountStatus;
import com.leandro.shop.user.entity.UserRole;
import jakarta.validation.constraints.*;

public record UserRegistrationByAdminRequest(
        @JsonProperty("first_name")
        @NotBlank(message = "First name is required")
        @Size(min=2, max=75, message = "First name must be between 2 and 75 characters")
        @Pattern(
                regexp = "^[\\p{L} '-]+$",
                message = "First name contains invalid characters"
        )
        String firstName,

        @JsonProperty("last_name")
        @NotBlank(message = "Last name is required")
        @Size(min=2, max=75, message = "Last name must be between 2 and 75 characters")
        @Pattern(
                regexp = "^[\\p{L} '-]+$",
                message = "Last name contains invalid characters"
        )
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 254, message = "Email is too long")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min=8, max=128,  message = "Password must be between 8 and 128 characters")
        String password,

        @JsonProperty("user_role")
        @NotNull(message = "User role is required")
        UserRole role,

        @JsonProperty("account_status")
        @NotNull(message = "Account status is required")
        AccountStatus status
){}
