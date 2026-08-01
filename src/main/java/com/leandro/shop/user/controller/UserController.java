package com.leandro.shop.user.controller;

import com.leandro.shop.shared.payload.AppResponse;
import com.leandro.shop.shared.payload.PageResponse;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Users", description = "User management for admins")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    private final UserService userService;


    @Operation(summary = "Get a user by ID", description = "Retrieves user details. Requires ADMIN authority.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<AppResponse<UserResponse>> getUser(
            @PathVariable UUID id
    ){
        return ResponseEntity.ok(
                AppResponse.success(
                        "User retrieved successfully",
                        userService.getUser(id)
                )
        );
    }

    @Operation(summary = "Get all users", description = "Retrieves all users details. Requires ADMIN authority.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
    })
    @GetMapping
    public ResponseEntity<AppResponse<PageResponse<UserResponse>>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ){
        return ResponseEntity.ok(
                AppResponse.success(
                        "Users retrieved successfully",
                        userService.getUsers(page, size)
                )
        );
    }

    @Operation(summary = "Create a new user", description = "Creates a new user by an admin.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<AppResponse<UserResponse>> createUser(
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
                        AppResponse.success("User created successfully", response)
                );
    }

    @Operation(summary = "Soft delete a user", description = "Soft deletes a user by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserSoft(
            @PathVariable UUID id
    ){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update a user", description = "Updates a user by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PatchMapping("/{id}")
    public ResponseEntity<AppResponse<UserResponse>> patchUser(
            @PathVariable UUID id,
            @Valid @RequestBody UserUpdateByAdminRequest request
            ){
        return ResponseEntity.ok(
                AppResponse.success(
                        "User updated successfully",
                        userService.updateUser(id, request)
                )
        );
    }
}
