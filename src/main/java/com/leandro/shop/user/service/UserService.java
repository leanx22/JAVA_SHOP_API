package com.leandro.shop.user.service;

import com.leandro.shop.shared.exceptions.*;
import com.leandro.shop.shared.security.CustomUserDetails;
import com.leandro.shop.user.dto.*;
import com.leandro.shop.user.entity.AccountStatus;
import com.leandro.shop.user.entity.User;
import com.leandro.shop.user.mapper.UserMapper;
import com.leandro.shop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper mapper;

    private final PasswordEncoder encoder;


    // PROFILE METHODS
    public UserResponse getCurrentUserProfile(){
        User currentUser = getAuthenticatedUser();
        return mapper.toResponse(currentUser);
    }

    public UserResponse updateCurrentUserProfile(UserUpdateRequest request){
        User user = getAuthenticatedUser();

        if(request.email() != null && !request.email().equals(user.getEmail())){
            if(userRepository.existsByEmail(request.email())){
                throw new ResourceAlreadyExistsException("Email already in use");
            }
        }

        mapper.updateEntityFromDto(request, user);
        User saved = userRepository.save(user);
        return mapper.toResponse(saved);
    }

    public void disableCurrentUserProfile(){
        User user = getAuthenticatedUser();
        user.setStatus(AccountStatus.DELETED);
        userRepository.save(user);
    }

    // SHARED METHODS
    public UserResponse getUser(UUID id){
        User user = userRepository.findById(id)
                .orElseThrow(
                        ()->new ResourceNotFoundException("User id "+id+" not found")
                );
        return mapper.toResponse(user);
    }

    // ADMIN METHODS
    public UserResponse createUser(UserRegistrationByAdminRequest request){

        if(userRepository.existsByEmail(request.email())) {
            throw new ResourceAlreadyExistsException("Email already in use");
        }

        User user = mapper.toEntity(request);
        String hashedPswrd = encoder.encode(request.password());
        user.setPassword(hashedPswrd);

        User saved = userRepository.save(user);
        return mapper.toResponse(saved);
    }

    public UserResponse updateUser(UUID userId, UserUpdateByAdminRequest request){
        User user = userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("User id "+userId+" not found"));

        if(request.email() != null && !request.email().equals(user.getEmail())){
            if(userRepository.existsByEmail(request.email())){
                throw new ResourceAlreadyExistsException("Email already in use");
            }
        }

        if(request.password()!=null && !request.password().isBlank()){
            String hashed = encoder.encode(request.password());
            user.setPassword(hashed);
        }

        mapper.updateEntityFromDto(request, user);
        return mapper.toResponse(userRepository.save(user));
    }

    public void deleteUser(UUID userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("User "+userId+" not found"));

        user.setStatus(AccountStatus.DELETED);
        userRepository.save(user);
    }


    private User getAuthenticatedUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || auth.getPrincipal() == null){
            throw new UnauthorizedException("Invalid authentication context");
        }

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        return userDetails.getUser();
    }

}
