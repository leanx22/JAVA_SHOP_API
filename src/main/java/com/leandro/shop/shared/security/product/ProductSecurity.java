package com.leandro.shop.shared.security.product;


import com.leandro.shop.product.entity.Product;
import com.leandro.shop.product.entity.ProductImage;
import com.leandro.shop.product.repository.ProductImageRepository;
import com.leandro.shop.product.repository.ProductRepository;
import com.leandro.shop.shared.security.user.CustomUserDetails;
import com.leandro.shop.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("productSecurity")
@RequiredArgsConstructor
public class ProductSecurity {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    public boolean isOwner(Authentication authentication, UUID productUUID){
        Product product = productRepository.findById(productUUID).orElse(null);
        if(product == null) return false;

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        return product.getSeller().getId().equals(user.getId());
    }

    public boolean isImageOwner(Authentication authentication, UUID imageId){
        ProductImage image = productImageRepository.findById(imageId).orElse(null);
        if(image == null) return false;

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        return image.getProduct().getSeller().getId().equals(user.getId());
    }

}
