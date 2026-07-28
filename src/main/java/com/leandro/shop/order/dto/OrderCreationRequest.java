package com.leandro.shop.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderCreationRequest(
        @NotEmpty List<@Valid OrderItemRequest> items,
        String paymentToken
){}
