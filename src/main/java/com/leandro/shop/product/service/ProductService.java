package com.leandro.shop.product.service;

import com.leandro.shop.product.dto.ProductCreationRequest;
import com.leandro.shop.product.dto.ProductResponse;
import com.leandro.shop.product.entity.Product;
import com.leandro.shop.product.mapper.ProductMapper;
import com.leandro.shop.product.repository.ProductRepository;
import com.leandro.shop.shared.exceptions.ResourceNotFoundException;
import com.leandro.shop.shared.exceptions.UnauthorizedException;
import com.leandro.shop.shared.payload.PageResponse;
import com.leandro.shop.user.entity.User;
import com.leandro.shop.user.repository.UserRepository;
import com.leandro.shop.user.service.UserService;
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth == null || !(auth.getPrincipal() instanceof String userId)){
            throw new UnauthorizedException("Invalid authentication context");
        }

        UUID sellerId = UUID.fromString(userId);

        User seller = userRepository.findById(sellerId).orElseThrow(
                ()->new ResourceNotFoundException("Seller not found")
        );

        Product product = mapper.toEntity(request);
        product.setSeller(seller);

        Product saved = productRepository.save(product);
        return mapper.toResponse(saved);
    }

    public PageResponse<ProductResponse> getAllProducts(int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Product> productPage = productRepository.findAll(pageRequest);

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
