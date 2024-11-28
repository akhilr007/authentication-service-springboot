package com.akhil.expensetracker.authservice.repositories;

import com.akhil.expensetracker.authservice.entities.RefreshToken;
import com.akhil.expensetracker.authservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);
}