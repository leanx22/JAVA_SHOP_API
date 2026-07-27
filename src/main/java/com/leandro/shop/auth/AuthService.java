package com.leandro.shop.auth;

import com.leandro.shop.shared.exceptions.ResourceAlreadyExistsException;
import com.leandro.shop.shared.exceptions.UnauthorizedException;
import com.leandro.shop.user.dto.*;
import com.leandro.shop.user.entity.AccountStatus;
import com.leandro.shop.user.entity.User;
import com.leandro.shop.user.entity.UserRole;
import com.leandro.shop.user.mapper.UserMapper;
import com.leandro.shop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public UserRegistrationResponse registerUser(UserRegistrationRequest request){
        if(userRepository.existsByEmail(request.email())) throw new ResourceAlreadyExistsException("Email already in use");

        User user = mapper.toEntity(request);
        String hashedPswrd = encoder.encode(request.password());

        user.setStatus(AccountStatus.ACTIVE);
        user.setRole(UserRole.USER);
        user.setPassword(hashedPswrd);

        User savedUser = userRepository.save(user);

        UserResponse userResponse = mapper.toResponse(savedUser);

        HashMap<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("user_role", savedUser.getRole());
        extraClaims.put("account_status", savedUser.getStatus());
        String generatedToken = jwtService.generateToken(extraClaims, savedUser.getId());

        return new UserRegistrationResponse(userResponse, generatedToken);
    }

    public UserLoginResponse loginUser(UserLoginRequest request){
        User user = userRepository
                .findByEmail(request.email())
                .orElseThrow(()->new UnauthorizedException("Bad credentials")
                );

        boolean isPasswordCorrect = encoder.matches(request.password(), user.getPassword());
        if(!isPasswordCorrect) throw new UnauthorizedException("Bad credentials");

        HashMap<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("user_role", user.getRole());
        extraClaims.put("account_status", user.getStatus());
        String generatedToken = jwtService.generateToken(extraClaims, user.getId());
        return new UserLoginResponse(generatedToken, "Bearer", jwtService.getExpirationTime());
    }

}
