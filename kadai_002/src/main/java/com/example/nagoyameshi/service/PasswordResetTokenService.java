package com.example.nagoyameshi.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.PasswordResetToken;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.PasswordResetTokenRepository;

@Service
public class PasswordResetTokenService {
    private final PasswordResetTokenRepository tokenRepository;

    public PasswordResetTokenService(PasswordResetTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public PasswordResetToken create(User user, String token, LocalDateTime expiresAt) {
        PasswordResetToken prt = new PasswordResetToken();
        prt.setUser(user);
        prt.setToken(token);
        prt.setExpiresAt(expiresAt);
        return tokenRepository.save(prt);
    }

    public PasswordResetToken findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Transactional
    public void markUsed(PasswordResetToken prt) {
        prt.setUsed(true);
        tokenRepository.save(prt);
    }
}
