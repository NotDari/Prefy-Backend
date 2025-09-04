package com.daribear.PrefyBackend.Authentication.Registration.RegistrationToken;


import com.daribear.PrefyBackend.Authentication.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repository representing rthe handling of the RegistrationConfirmationToken.
 */
@Repository
public interface RegistrationConfirmationTokenRepository extends JpaRepository<RegistrationConfirmationToken, Long> {

    /**
     * Finds a token given a value
     * @param token value to check
     * @return the RegistrationConfirmationToken if found
     */
    Optional<RegistrationConfirmationToken> findByToken(String token);

    /**
     * Finds a token with a given id
     * @param id id to use for checking
     * @return the RegistrationConfirmationToken if found
     */
    Optional<RegistrationConfirmationToken> findById(Long id);


    /**
     * Updates the token with the set value by updating the confirmedAt date.
     * @param token value of the token to be updated
     * @param confirmedAt  time to set confirmedAt
     * @return 1 if successful
     */
    @Transactional
    @Modifying
    @Query("UPDATE RegistrationConfirmationToken c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    int updateConfirmedAt(String token,
                          LocalDateTime confirmedAt);

    /**
     * Invalidates the token with the set value by updating the expiredAt date.
     * @param token the value of the token
     * @param expiredAt time to set expiredAt
     * @return 1 if successful
     */
    @Transactional
    @Modifying
    @Query("UPDATE RegistrationConfirmationToken c " +
            "SET c.expiredAt = ?2 " +
            "WHERE c.token = ?1")
    int invalidateToken(String token,
                        LocalDateTime expiredAt);

    /**
     * Since the token has been resent updates the createdAt and the expiredAt to make it valid.
     * @param authentication the authentication entity associated with the token
     * @param createdAt time to set createdAt
     * @param expiredAt time to set expiredAt
     * @param token the value of the token
     * @return 1 if successful
     */
    @Transactional
    @Modifying
    @Query("UPDATE RegistrationConfirmationToken c " +
            "SET c.createdAt = ?2" +
            ", c.expiredAt = ?3 " +
            ", c.token = ?4 " +
            "WHERE c.authentication = ?1")
    int tokenReSent(Authentication authentication,
                    LocalDateTime createdAt, LocalDateTime expiredAt, String token);

}
