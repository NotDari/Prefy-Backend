package com.daribear.PrefyBackend.JWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class JWTActions {

    public static DecodedJWT getJWT(JWTConfig jwtConfig,String header){
        String token = header.replace(jwtConfig.getTokenPrefix(), "");
        Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecretKey());
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        return verifier.verify(token);
    }
}
