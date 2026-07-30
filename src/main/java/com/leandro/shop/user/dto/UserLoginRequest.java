package com.leandro.shop.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request object for user authentication (login)")
public record UserLoginRequest(
        @Schema(description = "The email of the user", example = "user@example.com")
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @Schema(description = "The password of the user", example = "password123!")
        @NotBlank(message = "Password is required")
        String password
){}
