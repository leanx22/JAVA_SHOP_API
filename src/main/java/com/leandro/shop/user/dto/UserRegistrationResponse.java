package com.leandro.shop.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserRegistrationResponse(
        UserResponse user,

        @JsonProperty("access_token")
        String accessToken
){}
