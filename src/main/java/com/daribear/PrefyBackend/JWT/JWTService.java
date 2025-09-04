package com.daribear.PrefyBackend.JWT;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.daribear.PrefyBackend.Authentication.Authentication;
import com.daribear.PrefyBackend.Authentication.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

/**
 * A service class for handling the jwttoken Entity.
 */
@Service
@AllArgsConstructor
public class JWTService {

    private JWTRepository jwtRepository;
    private AuthenticationService authService;

    /**
     * Logout the user by invalidating the jwt token
     *
     * @param jwt the token to invalidate
     */
    public void logout(DecodedJWT jwt){
        Optional<Authentication> optionalAuth;
        if (jwt.getSubject().contains("@")){
            optionalAuth = authService.getUserByEmail(jwt.getSubject());
        } else {
            optionalAuth = authService.getUserByEmail(jwt.getSubject());
        }
        if (optionalAuth.isPresent()){
            jwtRepository.logoutToken(jwt.getToken(), LocalDateTime.now());
        }


    }

    /**
     * Add a jwt token to the repository
     * @param jwtClass token to be added
     */
    public void saveToken(JWTClass jwtClass){
        jwtRepository.save(jwtClass);
    }

    /**
     * Check if token exists in repository
     * @param token token to be checked
     * @return whether or not the token exists in the repository
     */
    public boolean tokenExists(String token){
        return jwtRepository.findByToken(token).isPresent();
    }

    /**
     * Check whether a token is valid by checking the date is before the ban and expiry date
     * @param token token t be checked
     * @return whether or not the token is valid
     */
    public boolean tokenValid(String token){
        Optional<JWTClass> jwtClassOpt = jwtRepository.findByToken(token);
        //Invalid token check
        if (jwtClassOpt.isEmpty()){
            return false;
        }
        JWTClass jwtClass = jwtClassOpt.get();
        //Check ban date
        if (jwtClass.getBanDate() != null){
            if (jwtClass.getBanDate().isBefore(LocalDateTime.now())){
                return false;
            }
        }
        //Check expiry date
        if (jwtClass.getExpiryDate().isBefore(LocalDateTime.now())){
            return false;
        }
        return true;
    }
}
