package com.leandro.shop.product.controller;

import com.leandro.shop.product.dto.productImage.ProductImageResponse;
import com.leandro.shop.product.service.ProductImageService;
import com.leandro.shop.shared.payload.AppResponse;
import com.leandro.shop.shared.security.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Product Images", description = "Product image management")
@RestController
@RequestMapping("/products/images")
@RequiredArgsConstructor
public class ProductImageController {

    private final ProductImageService imageService;

    @Operation(
            summary = "Get product images",
            description = "Retrieves all images associated with a specific product."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Images retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<AppResponse<List<ProductImageResponse>>> getProductImages(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ){
        return ResponseEntity.ok(
                AppResponse.success(
                        "Images retrieved successfully",
                        imageService.getProductImages(id, userDetails.getUser())
                )
        );
    }

    @Operation(
            summary = "Upload product images",
            description = "Uploads one or more images for a specific product. Only the owner can upload."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Images uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or file", content = @Content),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not authorized to upload to this product", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @PostMapping("/{id}")
    @PreAuthorize("@productSecurity.isOwner(authentication, #id)")
    public ResponseEntity<AppResponse<List<ProductImageResponse>>> uploadProductImages(
            @PathVariable UUID id,
            @RequestParam("files") List<MultipartFile> files
    ){
        List<ProductImageResponse> images = imageService.addImagesToProduct(id, files);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .build()
                .toUri();

        return ResponseEntity
                .created(location)
                .body(
                    AppResponse.success("Images uploaded successfully", images)
                );
    }

    @Operation(
            summary = "Delete a product image",
            description = "Deletes a specific product image by its ID. Only the owner or an admin can delete."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Image deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not authorized to delete this image", content = @Content),
            @ApiResponse(responseCode = "404", description = "Image not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("@productSecurity.isImageOwner(authentication, #id) or hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteImage(
            @PathVariable UUID id
    ){
        imageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}
