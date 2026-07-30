package com.leandro.shop.product.mapper;

import com.leandro.shop.product.dto.product.ProductCreationRequest;
import com.leandro.shop.product.dto.product.ProductResponse;
import com.leandro.shop.product.dto.product.ProductUpdateRequest;
import com.leandro.shop.product.entity.Product;
import com.leandro.shop.user.dto.UserShortResponse;
import com.leandro.shop.user.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponse toResponse(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "seller", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(ProductCreationRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "seller", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(ProductUpdateRequest request, @MappingTarget Product product);

    UserShortResponse toUserShortResponse(User user);
}
