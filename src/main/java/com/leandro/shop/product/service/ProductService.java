package com.leandro.shop.product.service;

import com.leandro.shop.product.dto.product.ProductCreationRequest;
import com.leandro.shop.product.dto.product.ProductResponse;
import com.leandro.shop.product.dto.product.ProductUpdateRequest;
import com.leandro.shop.product.entity.Product;
import com.leandro.shop.product.mapper.ProductMapper;
import com.leandro.shop.product.repository.ProductRepository;
import com.leandro.shop.shared.exceptions.ResourceNotFoundException;
import com.leandro.shop.shared.payload.PageResponse;
import com.leandro.shop.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    public ProductResponse createProduct(ProductCreationRequest request, User seller){
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
            User user,
            int page,
            int size
    ){
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Product> productPage = productRepository.findBySeller(user, pageRequest);
        return buildPageResponse(productPage);
    }

    public ProductResponse updateProduct(UUID productId, ProductUpdateRequest request, User user){
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product id: "+productId+" not found"));
        mapper.updateEntityFromDto(request, product);
        return mapper.toResponse(productRepository.save(product));
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

}
