package com.leandro.shop.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        Boolean active,
        @JsonProperty("seller_id")
        UUID sellerId,
        @JsonProperty("updated_at")
        LocalDateTime updatedAt,
        @JsonProperty("created_at")
        LocalDateTime createdAt
){}
