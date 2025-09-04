package com.daribear.PrefyBackend.Authentication;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * The repository for handling the authentication data entity.
 */
@Repository
@Transactional(readOnly = true)
public interface AuthenticationRepository extends JpaRepository<Authentication, Long> {

    /**
     * Finds an authentication entity given the email
     * @param email email to use
     * @return the authentication entity if found
     */
    Optional<Authentication> findByEmail(String email);

    /**
     * Enable an account with a given email
     * @param email email for the account to authenticate
     * @return 1 if successful
     */
    @Transactional
    @Modifying
    @Query("UPDATE Authentication a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableAuthentication(String email);

    /**
     * Find an authentication with a given id
     * @param id id to use
     * @return Authentication if found
     */
    Optional<Authentication> findById(Long id);

    /**
     * Alter the password of the authentication
     * @param id id of the authentication
     * @param password new password to use
     * @return 1 if successful
     */
    @Transactional
    @Modifying
    @Query("UPDATE Authentication a " +
            "SET a.password = ?2 WHERE a.id = ?1")
    int alterPassword(Long id, String password);

}
