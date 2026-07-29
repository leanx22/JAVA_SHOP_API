package com.leandro.shop.product.controller;

import com.leandro.shop.product.dto.productImage.ProductImageResponse;
import com.leandro.shop.product.service.ProductImageService;
import com.leandro.shop.shared.payload.ApiResponse;
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

@RestController
@RequestMapping("/products/images")
@RequiredArgsConstructor
public class ProductImageController {

    private final ProductImageService imageService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<List<ProductImageResponse>>> getProductImages(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ){
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Images retrieved successfully",
                        imageService.getProductImages(id, userDetails.getUser())
                )
        );
    }

    @PostMapping("/{id}")
    @PreAuthorize("@productSecurity.isOwner(authentication, #id)")
    public ResponseEntity<ApiResponse<List<ProductImageResponse>>> uploadProductImages(
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
                    ApiResponse.success("Images uploaded successfully", images)
                );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@productSecurity.isOwner(authentication, #id) or hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteImage(
            @PathVariable UUID id
    ){
        imageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}
