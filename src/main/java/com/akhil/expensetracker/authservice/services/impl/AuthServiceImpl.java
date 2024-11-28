package com.akhil.expensetracker.authservice.services.impl;

import com.akhil.expensetracker.authservice.dto.*;
import com.akhil.expensetracker.authservice.entities.RefreshToken;
import com.akhil.expensetracker.authservice.entities.User;
import com.akhil.expensetracker.authservice.entities.UserRole;
import com.akhil.expensetracker.authservice.enums.RoleType;
import com.akhil.expensetracker.authservice.repositories.RefreshTokenRepository;
import com.akhil.expensetracker.authservice.security.JwtService;
import com.akhil.expensetracker.authservice.services.AuthService;
import com.akhil.expensetracker.authservice.services.CustomUserDetailsService;
import com.akhil.expensetracker.authservice.services.RefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final CustomUserDetailsService userService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    @Override
    public SuccessfulSignupDTO signup(RegisterUserDTO registerUserDTO) {
        User existingUser = userService.findByEmail(registerUserDTO.getEmail());
        if (existingUser != null) {
            throw new RuntimeException("User already exists");
        }

        User user = modelMapper.map(registerUserDTO, User.class);
        user.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));
        user.setRoles(Set.of(
                UserRole.builder()
                        .roleType(RoleType.USER)
                        .build())
        );

        User savedUser = userService.save(user);
        return modelMapper.map(savedUser, SuccessfulSignupDTO.class);
    }

    public LoginResponseDTO login(LoginUserDTO userDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword())
        );

        User user = (User) authentication.getPrincipal();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        String accessToken = jwtService.generateAccessToken(user);

        return LoginResponseDTO.builder()
                .accessToken(accessToken)
                .token(refreshToken.getToken())
                .build();

    }

    @Override
    public LoginResponseDTO refresh(String token) {
        return refreshTokenService.findByToken(token)
                    .map(refreshTokenService::verifyRefreshToken)
                    .map(RefreshToken::getUser)
                    .map(user -> {
                        String accessToken = jwtService.generateAccessToken(user);
                        return LoginResponseDTO.builder()
                                .accessToken(accessToken)
                                .token(token)
                                .build();
                    })
                    .orElseThrow(() -> new RuntimeException("Refresh token not found"));
    }

    @Override
    public LogoutDTO logout(String token) {
        refreshTokenService.findByToken(token).ifPresent(refreshTokenService::deleteRefreshToken);
        return LogoutDTO.builder()
                .message("Logged out successfully")
                .success(true)
                .build();
    }
}
