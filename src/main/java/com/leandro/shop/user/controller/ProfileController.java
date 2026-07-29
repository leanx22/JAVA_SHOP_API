package com.leandro.shop.user.controller;

import com.leandro.shop.shared.payload.ApiResponse;
import com.leandro.shop.shared.security.user.CustomUserDetails;
import com.leandro.shop.user.dto.UserResponse;
import com.leandro.shop.user.dto.UserUpdateRequest;
import com.leandro.shop.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUserProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return ResponseEntity.ok(
                ApiResponse.success(
                        "User profile retrieved successfully",
                        userService.getCurrentUserProfile(userDetails.getUser()))
        );
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @Valid @RequestBody UserUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Profile updated successfully",
                        userService.updateCurrentUserProfile(request, userDetails.getUser())
                )
        );
    }

    @DeleteMapping
    public ResponseEntity<Void> disableCurrentUserProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        userService.disableCurrentUserProfile(userDetails.getUser());
        return ResponseEntity.noContent().build();
    }

}
