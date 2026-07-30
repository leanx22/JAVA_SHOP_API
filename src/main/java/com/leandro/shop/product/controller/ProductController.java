package com.leandro.shop.product.controller;

import com.leandro.shop.product.dto.product.ProductCreationRequest;
import com.leandro.shop.product.dto.product.ProductResponse;
import com.leandro.shop.product.dto.product.ProductUpdateRequest;
import com.leandro.shop.product.service.ProductService;
import com.leandro.shop.shared.payload.AppResponse;
import com.leandro.shop.shared.payload.PageResponse;
import com.leandro.shop.shared.security.user.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;


@Tag(name = "Products", description = "Product management")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @Operation(
            summary = "Create a new product",
            description = "Creates a product associated with the authenticated seller. Defaults to active."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token", content = @Content)
    })
    @PostMapping
    public ResponseEntity<AppResponse<ProductResponse>> createProduct(
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
                        AppResponse.success("Product created successfully", createdProduct)
                );
    }

    @Operation(
            summary = "Get all products",
            description = "Retrieves a paginated list of public products with optional search functionality."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<AppResponse<PageResponse<ProductResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search
    ){
        return ResponseEntity.ok(
                AppResponse.success(
                        "Products retrieved successfully",
                        productService.getPublicProducts(page, size, search)
                )
        );
    }

    @Operation(
            summary = "Get current user's products",
            description = "Retrieves a paginated list of products belonging to the authenticated seller."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token", content = @Content)
    })
    @GetMapping("/me")
    public ResponseEntity<AppResponse<PageResponse<ProductResponse>>> getCurrentUserProducts(
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "10") int size,
             @AuthenticationPrincipal CustomUserDetails userDetails
    ){
         return ResponseEntity.ok(
                 AppResponse.success(
                         "Products retrieved successfully",
                         productService.getCurrentUserProducts(userDetails.getUser(), page, size)
                 )
         );
    }

    @Operation(
            summary = "Update an existing product",
            description = "Updates product details. Only the owner or an admin can update."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not authorized to update this product", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @PatchMapping("/{id}")
    @PreAuthorize("@productSecurity.isOwner(authentication, #id) or hasAuthority('ADMIN')")
    public ResponseEntity<AppResponse<ProductResponse>> updateProduct(
            @PathVariable UUID id,
            @Valid ProductUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return ResponseEntity.ok(
                AppResponse.success(
                        "Product updated successfully",
                        productService.updateProduct(id, request, userDetails.getUser())
                )
        );
    }

}
