package com.leandro.shop.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request object for updating the current user's profile")
public record UserUpdateRequest(
        @Schema(description = "The new first name of the user", example = "Jane")
        @JsonProperty("first_name")
        @Size(min=2, max=75, message = "First name must be between 2 and 75 characters")
        @Pattern(
                regexp = "^[\\p{L} '-]+$",
                message = "First name contains invalid characters"
        )
        String firstName,

        @Schema(description = "The new last name of the user", example = "Doe")
        @JsonProperty("last_name")
        @Size(min=2, max=75, message = "Last name must be between 2 and 75 characters")
        @Pattern(
                regexp = "^[\\p{L} '-]+$",
                message = "Last name contains invalid characters"
        )
        String lastName,

        @Schema(description = "The new email of the user", example = "jane.doe@example.com")
        @Email(message = "Email must be valid")
        @Size(max = 254, message = "Email is too long")
        String email
){}
