package com.leandro.shop.auth;

import com.leandro.shop.shared.payload.AppResponse;
import com.leandro.shop.user.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Authentication", description = "Authentication and registration management")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register a new user", description = "Registers a new user and returns an access token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registration completed"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping("/registration")
    public ResponseEntity<AppResponse<UserRegistrationResponse>> registerUser(
        @Valid @RequestBody UserRegistrationRequest request
    ){
        UserRegistrationResponse response = authService.registerUser(request);
        return ResponseEntity.ok(
                AppResponse.success("User registration completed", response)
        );
    }

    @Operation(summary = "User login", description = "Authenticates a user and returns an access token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login success"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<AppResponse<UserLoginResponse>> loginUser(
        @Valid @RequestBody UserLoginRequest request
    ){
        return ResponseEntity.ok(
            AppResponse.success("Login success", authService.loginUser(request)));
    }

    @Operation(summary = "Update password", description = "Updates the authenticated user's password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Password updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token", content = @Content)
    })
    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(
            @Valid @RequestBody UserChangePasswordRequest request
            ){
        authService.updatePassword(request);
        return ResponseEntity.noContent().build();
    }

}
