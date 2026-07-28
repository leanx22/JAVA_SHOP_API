package com.leandro.shop.auth;

import com.leandro.shop.shared.payload.ApiResponse;
import com.leandro.shop.user.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registration")
    public ResponseEntity<ApiResponse<UserRegistrationResponse>> registerUser(
        @Valid @RequestBody UserRegistrationRequest request
    ){
        UserRegistrationResponse response = authService.registerUser(request);
        return ResponseEntity.ok(
                ApiResponse.success("User registration completed", response)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserLoginResponse>> loginUser(
        @Valid @RequestBody UserLoginRequest request
    ){
        return ResponseEntity.ok(
            ApiResponse.success("Login success", authService.loginUser(request)));
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(
            @Valid @RequestBody UserChangePasswordRequest request
            ){
        authService.updatePassword(request);
        return ResponseEntity.noContent().build();
    }

}
