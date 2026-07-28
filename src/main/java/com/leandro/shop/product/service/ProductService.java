package com.leandro.shop.product.service;

import com.leandro.shop.product.dto.product.ProductCreationRequest;
import com.leandro.shop.product.dto.product.ProductResponse;
import com.leandro.shop.product.entity.Product;
import com.leandro.shop.product.mapper.ProductMapper;
import com.leandro.shop.product.repository.ProductRepository;
import com.leandro.shop.shared.exceptions.BadRequestException;
import com.leandro.shop.shared.exceptions.ResourceNotFoundException;
import com.leandro.shop.shared.exceptions.UnauthorizedException;
import com.leandro.shop.shared.payload.PageResponse;
import com.leandro.shop.shared.security.CustomUserDetails;
import com.leandro.shop.user.entity.User;
import com.leandro.shop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    public ProductResponse createProduct(ProductCreationRequest request){
        User seller = getAuthenticatedUser();

        Product product = mapper.toEntity(request);
        product.setSeller(seller);

        Product saved = productRepository.save(product);
        return mapper.toResponse(saved);
    }

    public PageResponse<ProductResponse> getPublicProducts(
            int page,
            int size,
            String search
    ){
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("price").descending());
        Page<Product> productPage;

        if(search == null || search.isBlank()){
            productPage = productRepository.findAllByActiveTrue(pageRequest);
        }else{
            productPage = productRepository.findByNameContainingIgnoreCaseAndActiveTrue(search, pageRequest);
        }

        return buildPageResponse(productPage);
    }

    public PageResponse<ProductResponse> getCurrentUserProducts(
            int page,
            int size
    ){
        User seller = getAuthenticatedUser();
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Product> productPage = productRepository.findBySeller(seller, pageRequest);
        return buildPageResponse(productPage);
    }

    public PageResponse<ProductResponse> getAllSellerProductsForAdmin(
            int page,
            int size,
            UUID seller
    ){
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("price").descending());
        Page<Product> productPage;

        User sellerEntity = userRepository.findById(seller).orElseThrow(
                ()->new ResourceNotFoundException("Seller not found")
        );

        productPage = productRepository.findBySeller(sellerEntity, pageRequest);
        return buildPageResponse(productPage);
    }

    private PageResponse<ProductResponse> buildPageResponse(Page<Product> productPage) {
        List<ProductResponse> responseList = productPage.getContent().stream()
                .map(mapper::toResponse)
                .toList();

        return new PageResponse<>(
                responseList,
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isLast()
        );
    }

    private User getAuthenticatedUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || auth.getPrincipal() == null){
            throw new UnauthorizedException("Invalid authentication context");
        }

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        return userDetails.getUser();
    }

}
