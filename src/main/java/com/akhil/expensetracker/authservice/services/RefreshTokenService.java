package com.akhil.expensetracker.authservice.services;


import com.akhil.expensetracker.authservice.entities.RefreshToken;
import com.akhil.expensetracker.authservice.entities.User;

import java.util.Optional;


public interface RefreshTokenService {
    RefreshToken createRefreshToken(User user);
    RefreshToken verifyRefreshToken(RefreshToken token);
    Optional<RefreshToken> findByToken(String token);
    void deleteRefreshToken(RefreshToken token);
}
