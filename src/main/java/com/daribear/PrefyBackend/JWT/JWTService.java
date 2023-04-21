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
            jwtRepository.logoutToken(jwt.getToken(), LocalDateTime.now());
        }

        return false;
    }


    public void saveToken(JWTClass jwtClass){
        jwtRepository.save(jwtClass);
    }
    public boolean tokenExists(String token){
        return jwtRepository.findByToken(token).isPresent();
    }

    public boolean tokenValid(String token){
        Optional<JWTClass> jwtClassOpt = jwtRepository.findByToken(token);
        if (jwtClassOpt.isEmpty()){
            return false;
        }
        JWTClass jwtClass = jwtClassOpt.get();
        if (jwtClass.getBanDate() != null){
            if (jwtClass.getBanDate().isBefore(LocalDateTime.now())){
                return false;
            }
        }
        if (jwtClass.getExpiryDate().isBefore(LocalDateTime.now())){
            return false;
        }
        return true;
    }
}
