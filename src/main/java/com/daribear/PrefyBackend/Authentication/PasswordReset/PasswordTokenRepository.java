package com.daribear.PrefyBackend.Authentication.PasswordReset;

import com.daribear.PrefyBackend.Authentication.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);

    @Query(
            value = "SELECT p.authentication FROM PasswordResetToken p WHERE p.token = ?1")
    Authentication getIdByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE PasswordResetToken p " +
            "SET p.confirmedAt = ?2 " +
            "WHERE p.token = ?1")
    int updateConfirmedAt(String token,
                          LocalDateTime confirmedAt);

}
