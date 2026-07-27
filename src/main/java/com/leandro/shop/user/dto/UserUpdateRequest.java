package com.leandro.shop.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
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
        String email
){}
