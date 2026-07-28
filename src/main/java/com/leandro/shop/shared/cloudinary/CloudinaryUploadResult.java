package com.leandro.shop.shared.cloudinary;

public record CloudinaryUploadResult(
        String publicId,
        String secureUrl
){}
