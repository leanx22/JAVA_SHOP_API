package com.leandro.shop.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserChangePasswordRequest(
        @JsonProperty("current_password")
        @NotBlank(message = "Current password is required")
        @Size(min=8, max=128,  message = "Password must be between 8 and 128 characters")
        String currentPassword,

        @JsonProperty("new_password")
        @NotBlank(message = "New password is required")
        @Size(min=8, max=128,  message = "Password must be between 8 and 128 characters")
        String newPassword
){
}
