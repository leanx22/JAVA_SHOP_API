package com.leandro.shop.product.service;

import com.leandro.shop.product.dto.productImage.ProductImageResponse;
import com.leandro.shop.product.entity.Product;
import com.leandro.shop.product.entity.ProductImage;
import com.leandro.shop.product.repository.ProductImageRepository;
import com.leandro.shop.product.repository.ProductRepository;
import com.leandro.shop.shared.cloudinary.CloudinaryService;
import com.leandro.shop.shared.exceptions.BadRequestException;
import com.leandro.shop.shared.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductImageService {

    private final ProductRepository productRepository;
    private final ProductImageRepository imageRepository;
    private final CloudinaryService cloudinaryService;

    public List<ProductImageResponse> getProductImages(UUID productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product not found"));
        return product.getImages().stream()
                .map(image ->
                        new ProductImageResponse(image.getId(), image.getImageUrl())
                ).toList();
    }

    public List<ProductImageResponse> addImagesToProduct(UUID productId, List<MultipartFile> files){

        Product product = productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product id: "+productId+" not found"));

        if(product.getImages().size() + files.size() > 5){
            throw new BadRequestException("This product would exceed the 5 images limit.");
        }

        if(files.size() > 5 ){
            throw new BadRequestException("You can only upload up to 5 images at a time.");
        }

        List<ProductImage> uploadedImages = files.parallelStream()
                .map(file -> {
                    var uploadResult = cloudinaryService.uploadImage(file, "shop_api/products");

                    ProductImage image = new ProductImage();
                    image.setImageUrl(uploadResult.secureUrl());
                    image.setCloudinaryId(uploadResult.publicId());
                    return image;
                })
                .toList();

        uploadedImages.forEach(product::addImage);
        productRepository.save(product);

        return uploadedImages.stream()
                .map(image ->
                        new ProductImageResponse(image.getId(), image.getImageUrl()))
                .toList();
    }


    public void deleteImage(UUID imageId){
        ProductImage image = imageRepository.findById(imageId)
                .orElseThrow(()->new ResourceNotFoundException("Image not found"));
        cloudinaryService.deleteImage(image.getCloudinaryId());

        Product product = image.getProduct();
        product.removeImage(image);
        imageRepository.delete(image);
    }

}
