package com.leandro.shop.user.controller;

import com.leandro.shop.shared.payload.ApiResponse;
import com.leandro.shop.user.dto.UserRegistrationByAdminRequest;
import com.leandro.shop.user.dto.UserResponse;
import com.leandro.shop.user.dto.UserUpdateByAdminRequest;
import com.leandro.shop.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    private final UserService userService;


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(
            @PathVariable UUID id
    ){
        return ResponseEntity.ok(
                ApiResponse.success(
                        "User retrieved successfully",
                        userService.getUser(id)
                )
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody UserRegistrationByAdminRequest request
    ){
        UserResponse response = userService.createUser(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(
                        ApiResponse.success("User created successfully", response)
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserSoft(
            @PathVariable UUID id
    ){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> patchUser(
            @PathVariable UUID id,
            @Valid @RequestBody UserUpdateByAdminRequest request
            ){
        return ResponseEntity.ok(
                ApiResponse.success(
                        "User updated successfully",
                        userService.updateUser(id, request)
                )
        );
    }
}
