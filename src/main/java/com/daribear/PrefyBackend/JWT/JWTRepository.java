package com.daribear.PrefyBackend.JWT;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JWTRepository  extends JpaRepository<JWTClass, Long> {
    Optional<JWTClass> findByToken(String token);
}
