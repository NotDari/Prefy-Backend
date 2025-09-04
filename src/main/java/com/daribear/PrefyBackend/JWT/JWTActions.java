package com.daribear.PrefyBackend.JWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

/**
 * This class represents JWTActions, so the actions to do with the jwt token.
 */
public class JWTActions {
    /**
     * Retrieves the JWT token from the header
     *
     * @param jwtConfig the jwtconfig from the jwt application, containing the prefix
     * @param header the HTTP header containing the JWT token aswell as the custom prefix.
     * @return a DecodedJWT representing the verified token
     */
    public static DecodedJWT getJWT(JWTConfig jwtConfig,String header){
        String token = header.replace(jwtConfig.getTokenPrefix(), "");
        Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecretKey());
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        return verifier.verify(token);
    }
}
