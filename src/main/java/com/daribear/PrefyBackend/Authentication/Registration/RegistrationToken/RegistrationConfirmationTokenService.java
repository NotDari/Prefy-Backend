package com.daribear.PrefyBackend.Authentication.Registration.RegistrationToken;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


/**
 * service for handling registration confirmation tokens.
 */
@Service
@AllArgsConstructor
public class RegistrationConfirmationTokenService {

    private final RegistrationConfirmationTokenRepository confirmationTokenRepository;

    /**
     * Save token to the repository.
     * @param token token to be saved
     */
    public void saveConfirmationToken(RegistrationConfirmationToken token){
        confirmationTokenRepository.save(token);
    }

    /**
     * Gets a token from the repository given the value
     * @param token value to check
     * @return RegistrationConfirmationToken if exists
     */
    public Optional<RegistrationConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    /**
     * Sets the token's confirmed time as now to mark it as confirmed.
     * @param token token to mark
     * @return 1 if successful
     */
    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }

    /**
     * Retrieves the token by using the authentication id;
     * @param id the id to retrieve the token
     * @return the RegistrationConfirmation token if found
     */
    public Optional<RegistrationConfirmationToken> getTokenByAuthId(Long id){
        return confirmationTokenRepository.findById(id);
    }

    /**
     * Since a confirmation token has been sent, update the token's expiration date and creation date.
     * @param newToken nthe details to update with
     */
    public void resendToken(RegistrationConfirmationToken newToken){
        confirmationTokenRepository.tokenReSent(newToken.getAuthentication(), newToken.getCreatedAt(), newToken.getExpiredAt(), newToken.getToken());
    }

    /**
     * Invalidates a registration token by setting its expiredAt timestamp to the current time.
     * @param token token to invalidate
     * @return 1 if successful
     */
    public int invalidateToken(String token){
        return confirmationTokenRepository.invalidateToken(
                token, LocalDateTime.now()
        );
    }

}
