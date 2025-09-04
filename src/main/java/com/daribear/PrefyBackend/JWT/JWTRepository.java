package com.daribear.PrefyBackend.JWT;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repository for the handling of the JWT token entity.
 */
@Repository
public interface JWTRepository  extends JpaRepository<JWTClass, Long> {
    Optional<JWTClass> findByToken(String token);

    /**
     * Invalidates a token by setting the current date as the ban date.
     * Meaning the user is essentially logged out.
     *
     * @param token token to alter
     * @param currentTime time to set the ban date at
     * @return successful or not
     */
    @Transactional
    @Modifying
    @Query("UPDATE JWTClass j " +
            "SET j.banDate = ?2 WHERE j.token = ?1")
    int logoutToken(String token, LocalDateTime currentTime);
}
