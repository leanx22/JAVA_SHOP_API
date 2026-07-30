package com.leandro.shop.product.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leandro.shop.product.dto.productImage.ProductImageResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object representing a product")
public record ProductResponse(
        @Schema(description = "The unique identifier of the product", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,
        @Schema(description = "The name of the product", example = "Smartphone")
        String name,
        @Schema(description = "A detailed description of the product", example = "A high-end smartphone with a large screen.")
        String description,
        @Schema(description = "The price of the product", example = "599.99")
        BigDecimal price,
        @Schema(description = "Whether the product is active/visible", example = "true")
        Boolean active,
        @Schema(description = "List of images associated with the product")
        List<ProductImageResponse> images,
        @Schema(description = "The unique identifier of the seller who owns the product", example = "123e4567-e89b-12d3-a456-426614174000")
        @JsonProperty("seller_id")
        UUID sellerId,
        @Schema(description = "The date and time when the product was last updated", example = "2023-10-27T10:00:00")
        @JsonProperty("updated_at")
        LocalDateTime updatedAt,
        @Schema(description = "The date and time when the product was created", example = "2023-10-27T10:00:00")
        @JsonProperty("created_at")
        LocalDateTime createdAt
){}
