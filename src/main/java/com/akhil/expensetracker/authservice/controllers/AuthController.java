package com.akhil.expensetracker.authservice.controllers;

import com.akhil.expensetracker.authservice.dto.*;
import com.akhil.expensetracker.authservice.services.AuthService;
import com.akhil.expensetracker.authservice.services.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<SuccessfulSignupDTO> signup(@RequestBody RegisterUserDTO registerUserDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.signup(registerUserDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO>login(HttpServletResponse response, @RequestBody LoginUserDTO loginUserDTO) {
        LoginResponseDTO loginResponseDTO = authService.login(loginUserDTO);
        return getLoginResponseDTOResponseEntity(loginResponseDTO, response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refresh(HttpServletRequest request,
                                                    HttpServletResponse response){

        String refreshToken = getRefreshTokenFromCookie(request);
        LoginResponseDTO responseDTO = authService.refresh(refreshToken);
        return getLoginResponseDTOResponseEntity(responseDTO, response);
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutDTO> logout(HttpServletRequest request, HttpServletResponse response){

        String refreshToken = getRefreshTokenFromCookie(request);

        Cookie accessCookie = new Cookie("accessToken", "");
        accessCookie.setMaxAge(0);
        accessCookie.setHttpOnly(true);
        response.addCookie(accessCookie);

        Cookie refreshCookie = new Cookie("refreshToken", "");
        refreshCookie.setMaxAge(0);
        refreshCookie.setHttpOnly(true);
        response.addCookie(refreshCookie);

        return ResponseEntity.ok(authService.logout(refreshToken));
    }

    private ResponseEntity<LoginResponseDTO> getLoginResponseDTOResponseEntity(LoginResponseDTO loginResponseDTO,
                                                                               HttpServletResponse response) {
        String accessToken = loginResponseDTO.getAccessToken();
        Cookie accessCookie = new Cookie("accessToken", accessToken);
        accessCookie.setPath("/");
        accessCookie.setHttpOnly(true);
        response.addCookie(accessCookie);
        System.out.println("Access Cookie Path: " + accessCookie.getPath());


        String refreshToken = loginResponseDTO.getToken();
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setPath("/");
        refreshCookie.setHttpOnly(true);
        response.addCookie(refreshCookie);
        System.out.println("Refresh Cookie Path: " + refreshCookie.getPath());

        return ResponseEntity.ok(loginResponseDTO);
    }

    private String getRefreshTokenFromCookie(HttpServletRequest request){
        return  Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationServiceException("Refresh Token not found in cookies"));
    }

}
