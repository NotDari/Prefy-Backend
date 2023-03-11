package com.daribear.PrefyBackend.Authentication.PasswordReset;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;

@Service
@AllArgsConstructor
public class PasswordSecurity {
    private PasswordTokenRepository passwordTokenRepository;


    public String validatePasswordResetToken(String token) {
        final PasswordResetToken passToken = passwordTokenRepository.findByToken(token);
        return !isTokenFound(passToken) ? "invalidToken"
                : isTokenExpired(passToken) ? "expired"
                : isTokenConfirmed(passToken) ? "confirmed"
                : null;
    }

    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        final LocalDateTime currentTime = LocalDateTime.now();
        return passToken.getExpiredAt().isBefore(currentTime);
    }

    private boolean isTokenConfirmed(PasswordResetToken passToken){
        if (passToken.getConfirmedAt() != null){
            return true;
        } else {
            return false;
        }
    }
}
