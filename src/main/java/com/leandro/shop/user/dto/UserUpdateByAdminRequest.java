package com.leandro.shop.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leandro.shop.user.entity.AccountStatus;
import com.leandro.shop.user.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateByAdminRequest(
        @JsonProperty("first_name")
        @Size(min=2, max=75, message = "First name must be between 2 and 75 characters")
        @Pattern(
                regexp = "^[\\p{L} '-]+$",
                message = "First name contains invalid characters"
        )
        String firstName,

        @JsonProperty("last_name")
        @Size(min=2, max=75, message = "Last name must be between 2 and 75 characters")
        @Pattern(
                regexp = "^[\\p{L} '-]+$",
                message = "Last name contains invalid characters"
        )
        String lastName,

        @Email(message = "Email must be valid")
        @Size(max = 254, message = "Email is too long")
        String email,

        @JsonProperty("account_status")
        AccountStatus status,

        UserRole role,

        @Size(min=8, max=128,  message = "Password must be between 8 and 128 characters")
        String password
){}
