package com.daribear.PrefyBackend.Authentication.PasswordReset;

import com.daribear.PrefyBackend.Authentication.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * The repository which handles the password reset token data entity.
 */
@Repository
public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    /**
     * Finds a token by its value
     * @param token the token string
     * @return the token if found
     */
    PasswordResetToken findByToken(String token);

    /**
     * Gets the authentication entity from a password reset token.
     * @param token the password reset token
     * @return the authentication entity
     */
    @Query(
            value = "SELECT p.authentication FROM PasswordResetToken p WHERE p.token = ?1")
    Authentication getIdByToken(String token);

    /**
     * Confirm a token and provide the time it was confirmed
     * @param token token to be confirmed
     * @param confirmedAt timed to confirm the token
     * @return 1 if successful else error occured
     */
    @Transactional
    @Modifying
    @Query("UPDATE PasswordResetToken p " +
            "SET p.confirmedAt = ?2 " +
            "WHERE p.token = ?1")
    int updateConfirmedAt(String token,
                          LocalDateTime confirmedAt);

}
