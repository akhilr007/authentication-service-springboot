package com.akhil.expensetracker.authservice.services;

import com.akhil.expensetracker.authservice.dto.*;

public interface AuthService {
    SuccessfulSignupDTO signup(RegisterUserDTO user);

    LoginResponseDTO login(LoginUserDTO userDTO);

    LoginResponseDTO refresh(String token);

    LogoutDTO logout(String token);
}
