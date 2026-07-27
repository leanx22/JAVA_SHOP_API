package com.leandro.shop.user.controller;

import com.leandro.shop.shared.payload.ApiResponse;
import com.leandro.shop.user.dto.UserResponse;
import com.leandro.shop.user.dto.UserUpdateRequest;
import com.leandro.shop.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUserProfile(){
        return ResponseEntity.ok(
                ApiResponse.success(
                        "User profile retrieved successfully",
                        userService.getCurrentUserProfile())
        );
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @Valid @RequestBody UserUpdateRequest request
            ){
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Profile updated successfully",
                        userService.updateCurrentUserProfile(request)
                )
        );
    }

    @DeleteMapping
    public ResponseEntity<Void> disableCurrentUserProfile(){
        userService.disableCurrentUserProfile();
        return ResponseEntity.noContent().build();
    }

}
