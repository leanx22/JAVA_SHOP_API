package com.leandro.shop.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object returned after a successful self-registration")
public record UserRegistrationResponse(
        @Schema(description = "The details of the registered user")
        UserResponse user,

        @Schema(description = "The JWT access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        @JsonProperty("access_token")
        String accessToken
){}
