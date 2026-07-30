package com.leandro.shop.product.dto.product;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request object for updating an existing product")
public record ProductUpdateRequest(

        @Schema(description = "The new name of the product", example = "Smartphone Pro")
        @Size(min=2, max=250, message = "Product name must be between 2 and 250 characters")
        String name,

        @Schema(description = "The new detailed description of the product", example = "An upgraded high-end smartphone.")
        @Size(min=2, max=250, message = "Product description must be between 2 and 250 characters")
        String description,

        @Schema(description = "The new price of the product", example = "699.99")
        @DecimalMax(value = "99999999.99", message = "Price is too high")
        @Positive
        BigDecimal price,

        @Schema(description = "The new active status of the product", example = "false")
        Boolean active

){}
