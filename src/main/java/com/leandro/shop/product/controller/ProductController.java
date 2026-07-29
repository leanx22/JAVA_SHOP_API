package com.leandro.shop.product.controller;

import com.leandro.shop.product.dto.product.ProductCreationRequest;
import com.leandro.shop.product.dto.product.ProductResponse;
import com.leandro.shop.product.dto.product.ProductUpdateRequest;
import com.leandro.shop.product.service.ProductService;
import com.leandro.shop.shared.payload.ApiResponse;
import com.leandro.shop.shared.payload.PageResponse;
import com.leandro.shop.shared.security.user.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductCreationRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){

        ProductResponse createdProduct = productService.createProduct(request, userDetails.getUser());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdProduct.id())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(
                        ApiResponse.success("Product created successfully", createdProduct)
                );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search
    ){
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Products retrieved successfully",
                        productService.getPublicProducts(page, size, search)
                )
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getCurrentUserProducts(
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "10") int size,
             @AuthenticationPrincipal CustomUserDetails userDetails
    ){
         return ResponseEntity.ok(
                 ApiResponse.success(
                         "Products retrieved successfully",
                         productService.getCurrentUserProducts(userDetails.getUser(), page, size)
                 )
         );
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@productSecurity.isOwner(authentication, #id) or hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable UUID id,
            @Valid ProductUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Product updated successfully",
                        productService.updateProduct(id, request, userDetails.getUser())
                )
        );
    }

}
