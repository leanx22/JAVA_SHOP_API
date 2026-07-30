package com.leandro.shop.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object returned after a successful login")
public record UserLoginResponse(
        @Schema(description = "The JWT access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        @JsonProperty("access_token")
        String accessToken,

        @Schema(description = "The type of the token", example = "Bearer")
        @JsonProperty("token_type")
        String tokenType,

        @Schema(description = "The time in seconds until the token expires", example = "3600")
        @JsonProperty("expires_in")
        Long expiresIn
){}
