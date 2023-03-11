package com.daribear.PrefyBackend.Authentication.Registration.RegistrationToken;


import com.daribear.PrefyBackend.Authentication.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RegistrationConfirmationTokenRepository extends JpaRepository<RegistrationConfirmationToken, Long> {

    Optional<RegistrationConfirmationToken> findByToken(String token);

    Optional<RegistrationConfirmationToken> findById(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE RegistrationConfirmationToken c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    int updateConfirmedAt(String token,
                          LocalDateTime confirmedAt);

    @Transactional
    @Modifying
    @Query("UPDATE RegistrationConfirmationToken c " +
            "SET c.expiredAt = ?2 " +
            "WHERE c.token = ?1")
    int invalidateToken(String token,
                        LocalDateTime confirmedAt);

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
