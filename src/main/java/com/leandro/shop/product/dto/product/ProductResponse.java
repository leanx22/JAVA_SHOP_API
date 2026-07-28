package com.leandro.shop.product.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leandro.shop.product.dto.productImage.ProductImageResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        Boolean active,
        List<ProductImageResponse> images,
        @JsonProperty("seller_id")
        UUID sellerId,
        @JsonProperty("updated_at")
        LocalDateTime updatedAt,
        @JsonProperty("created_at")
        LocalDateTime createdAt
){}
