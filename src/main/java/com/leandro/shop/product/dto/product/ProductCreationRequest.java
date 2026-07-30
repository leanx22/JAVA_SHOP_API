package com.leandro.shop.product.dto.product;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request object for creating a new product")
public record ProductCreationRequest(
        @Schema(description = "The name of the product", example = "Smartphone", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(min=2, max=250, message = "Product name must be between 2 and 250 characters")
        String name,

        @Schema(description = "A detailed description of the product", example = "A high-end smartphone with a large screen.", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(min=2, max=250, message = "Product description must be between 2 and 250 characters")
        String description,

        @Schema(description = "The price of the product", example = "599.99", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "A price is required")
        @DecimalMax(value = "99999999.99", message = "Price is too high")
        @Positive
        BigDecimal price,

        @Schema(description = "Whether the product is active/visible", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "An initial status is needed")
        Boolean active
){}
