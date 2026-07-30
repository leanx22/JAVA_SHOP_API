package com.leandro.shop.product.dto.productImage;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object representing a product image")
public record ProductImageResponse(
        @Schema(description = "The unique identifier of the image", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,
        @Schema(description = "The URL of the image", example = "https://example.com/images/product.jpg")
        String imageUrl
){}
