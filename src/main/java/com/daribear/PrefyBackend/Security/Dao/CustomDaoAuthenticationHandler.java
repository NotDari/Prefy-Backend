package com.daribear.PrefyBackend.Security.Dao;

import org.apache.http.client.AuthenticationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class CustomDaoAuthenticationHandler extends DaoAuthenticationProvider {

    @Autowired
    CustomAuthDaoHandler authenticationHandler;



    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return authenticationHandler.authenticate(authentication);
    }



}
