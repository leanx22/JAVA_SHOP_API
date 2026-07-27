package com.leandro.shop.product.dto;

import com.leandro.shop.user.entity.User;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductCreationRequest(
        @NotBlank
        @Size(min=2, max=250, message = "Product name must be between 2 and 250 characters")
        String name,

        @NotBlank
        @Size(min=2, max=250, message = "Product description must be between 2 and 250 characters")
        String description,

        @NotNull(message = "A price is required")
        @DecimalMax(value = "99999999.99", message = "Price is too high")
        @Positive
        BigDecimal price,

        @NotNull(message = "An initial status is needed")
        Boolean active
){}
