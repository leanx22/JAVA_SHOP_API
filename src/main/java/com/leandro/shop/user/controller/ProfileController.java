package com.leandro.shop.user.controller;

import com.leandro.shop.shared.payload.AppResponse;
import com.leandro.shop.shared.security.user.CustomUserDetails;
import com.leandro.shop.user.dto.UserResponse;
import com.leandro.shop.user.dto.UserUpdateRequest;
import com.leandro.shop.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Profile", description = "User profile management")
@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @Operation(summary = "Get current user profile", description = "Retrieves the authenticated user's profile.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token", content = @Content)
    })
    @GetMapping
    public ResponseEntity<AppResponse<UserResponse>> getCurrentUserProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return ResponseEntity.ok(
                AppResponse.success(
                        "User profile retrieved successfully",
                        userService.getCurrentUserProfile(userDetails.getUser()))
        );
    }

    @Operation(summary = "Update current user profile", description = "Updates the authenticated user's profile.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token", content = @Content)
    })
    @PatchMapping
    public ResponseEntity<AppResponse<UserResponse>> updateProfile(
            @Valid @RequestBody UserUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return ResponseEntity.ok(
                AppResponse.success(
                        "Profile updated successfully",
                        userService.updateCurrentUserProfile(request, userDetails.getUser())
                )
        );
    }

    @Operation(summary = "Disable current user profile", description = "Soft deletes/disables the authenticated user's profile.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Profile disabled successfully"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token", content = @Content)
    })
    @DeleteMapping
    public ResponseEntity<Void> disableCurrentUserProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        userService.disableCurrentUserProfile(userDetails.getUser());
        return ResponseEntity.noContent().build();
    }

}
