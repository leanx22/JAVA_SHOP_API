package com.leandro.shop.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leandro.shop.user.entity.AccountStatus;
import com.leandro.shop.user.entity.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
    UUID id,

    @JsonProperty("first_name")
    String firstName,

    @JsonProperty("last_name")
    String lastName,

    String email,

    AccountStatus status,

    UserRole role,

    @JsonProperty("created_at")
    LocalDateTime createdAt,

    @JsonProperty("updated_at")
    LocalDateTime updatedAt
){}
