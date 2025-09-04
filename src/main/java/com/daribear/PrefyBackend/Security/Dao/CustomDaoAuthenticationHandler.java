package com.daribear.PrefyBackend.Security.Dao;

import org.apache.http.client.AuthenticationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Overrides Springboots default DAOAuthenticationProvider which is how springboot authenticates users against a database.
 * Has been overriden to call my customauthdaohandler.
 */
public class CustomDaoAuthenticationHandler extends DaoAuthenticationProvider {

    @Autowired
    CustomAuthDaoHandler authenticationHandler;


    /**
     * Calls my customauthdaohandler to authenticate the user.
     *
     * @param authentication Springboots authentication class which contains authentication details
     * @return Authentication, on successful authentication
     * @throws AuthenticationException error if failed authentication
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return authenticationHandler.authenticate(authentication);
    }



}
