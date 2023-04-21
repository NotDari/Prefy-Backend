package com.daribear.PrefyBackend.Authentication;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Repository
@Transactional(readOnly = true)
public interface AuthenticationRepository extends JpaRepository<Authentication, Long> {


    Optional<Authentication> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE Authentication a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableAuthentication(String email);

    Optional<Authentication> findById(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Authentication a " +
            "SET a.password = ?2 WHERE a.id = ?1")
    int alterPassword(Long id, String password);

}
