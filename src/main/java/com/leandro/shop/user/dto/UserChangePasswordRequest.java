package com.leandro.shop.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request object for changing the user's password")
public record UserChangePasswordRequest(
        @Schema(description = "The current password of the user", example = "oldPassword123!")
        @JsonProperty("current_password")
        @NotBlank(message = "Current password is required")
        @Size(min=8, max=128,  message = "Password must be between 8 and 128 characters")
        String currentPassword,

        @Schema(description = "The new password for the user", example = "newPassword456!")
        @JsonProperty("new_password")
        @NotBlank(message = "New password is required")
        @Size(min=8, max=128,  message = "Password must be between 8 and 128 characters")
        String newPassword
){}
