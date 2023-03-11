package com.daribear.PrefyBackend.Authentication.Registration.RegistrationToken;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RegistrationConfirmationTokenService {

    private final RegistrationConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(RegistrationConfirmationToken token){
        confirmationTokenRepository.save(token);
    }

    public Optional<RegistrationConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }

    public Optional<RegistrationConfirmationToken> getTokenByAuthId(Long id){
        return confirmationTokenRepository.findById(id);
    }

    public void resendToken(RegistrationConfirmationToken newToken){
        confirmationTokenRepository.tokenReSent(newToken.getAuthentication(), newToken.getCreatedAt(), newToken.getExpiredAt(), newToken.getToken());
    }
    public int invalidateToken(String token){
        return confirmationTokenRepository.invalidateToken(
                token, LocalDateTime.now()
        );
    }

}
