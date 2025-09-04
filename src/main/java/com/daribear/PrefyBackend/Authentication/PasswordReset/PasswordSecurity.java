package com.daribear.PrefyBackend.Authentication.PasswordReset;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;

/**
 * Service responsible for validating password reset tokens.
 */
@Service
@AllArgsConstructor
public class PasswordSecurity {
    private PasswordTokenRepository passwordTokenRepository;

    /**
     * Validates the password reset token, making sure its not confirmed/expired or invalid
     * @param token token to be checked
     * @return null if the token is valid
     */
    public String validatePasswordResetToken(String token) {
        final PasswordResetToken passToken = passwordTokenRepository.findByToken(token);
        return !isTokenFound(passToken) ? "invalidToken"
                : isTokenExpired(passToken) ? "expired"
                : isTokenConfirmed(passToken) ? "confirmed"
                : null;
    }

    /**
     * Checks whether the token exists in the repository
     * @param passToken the token to be checked
     * @return whether the token exists (true if it does)
     */
    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    /**
     * Checks if a token is expired
     * @param passToken the token to be checked
     * @return Whether the token is expired
     */
    private boolean isTokenExpired(PasswordResetToken passToken) {
        final LocalDateTime currentTime = LocalDateTime.now();
        return passToken.getExpiredAt().isBefore(currentTime);
    }

    /**
     * Checks if the token is confirmed.
     * @param passToken the token to be checked
     * @return whether the token has been confirmed or not
     */
    private boolean isTokenConfirmed(PasswordResetToken passToken){
        if (passToken.getConfirmedAt() != null){
            return true;
        } else {
            return false;
        }
    }
}
