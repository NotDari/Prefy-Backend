package com.daribear.PrefyBackend.JWT;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.daribear.PrefyBackend.Authentication.Authentication;
import com.daribear.PrefyBackend.Authentication.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
@AllArgsConstructor
public class JWTService {

    private JWTRepository jwtRepository;
    private AuthenticationService authService;


    public boolean logout(DecodedJWT jwt){
        Optional<Authentication> optionalAuth;
        if (jwt.getSubject().contains("@")){
            optionalAuth = authService.getUserByEmail(jwt.getSubject());
        } else {
            optionalAuth = authService.getUserByEmail(jwt.getSubject());
        }
        if (optionalAuth.isPresent()){
            JWTClass jwtClass = new JWTClass( jwt.getToken(), LocalDateTime.ofInstant(jwt.getIssuedAt().toInstant(), ZoneId.systemDefault()), LocalDateTime.ofInstant(jwt.getExpiresAt().toInstant(), ZoneId.systemDefault()), LocalDateTime.now(),optionalAuth.get());
            if (jwtRepository.findByToken(jwtClass.getToken()).isEmpty()){
                jwtRepository.save(jwtClass);
            }
        }

        return false;
    }

    public boolean tokenExists(String token){
        return jwtRepository.findByToken(token).isPresent();
    }
}
