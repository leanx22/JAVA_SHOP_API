package com.leandro.shop.product.mapper;

import com.leandro.shop.product.dto.ProductCreationRequest;
import com.leandro.shop.product.dto.ProductResponse;
import com.leandro.shop.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "sellerId", source = "seller.id")
    ProductResponse toResponse(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "seller", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(ProductCreationRequest request);
}
