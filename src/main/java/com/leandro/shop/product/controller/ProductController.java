package com.leandro.shop.product.controller;

import com.leandro.shop.product.dto.product.ProductCreationRequest;
import com.leandro.shop.product.dto.product.ProductResponse;
import com.leandro.shop.product.service.ProductService;
import com.leandro.shop.shared.payload.ApiResponse;
import com.leandro.shop.shared.payload.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductCreationRequest request
    ){

        ProductResponse createdProduct = productService.createProduct(request);

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
             @RequestParam(defaultValue = "10") int size
    ){
         return ResponseEntity.ok(
                 ApiResponse.success(
                         "Products retrieved successfully",
                         productService.getCurrentUserProducts(page, size)
                 )
         );
    }

    //TODO update y delete

}
