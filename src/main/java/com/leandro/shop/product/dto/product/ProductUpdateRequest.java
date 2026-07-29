package com.leandro.shop.product.dto.product;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductUpdateRequest(

        @Size(min=2, max=250, message = "Product name must be between 2 and 250 characters")
        String name,

        @Size(min=2, max=250, message = "Product description must be between 2 and 250 characters")
        String description,

        @DecimalMax(value = "99999999.99", message = "Price is too high")
        @Positive
        BigDecimal price,

        Boolean active

){}
