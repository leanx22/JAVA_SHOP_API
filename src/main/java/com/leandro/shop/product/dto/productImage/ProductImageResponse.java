package com.leandro.shop.product.dto.productImage;

import java.util.UUID;

public record ProductImageResponse(
        UUID id,
        String imageUrl
){}
