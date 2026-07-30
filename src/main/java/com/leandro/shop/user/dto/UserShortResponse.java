package com.leandro.shop.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record UserShortResponse(
        @JsonProperty("first_name")
        String firstName,

        @JsonProperty("last_name")
        String lastName,

        @JsonProperty("user_id")
        UUID id
){}
