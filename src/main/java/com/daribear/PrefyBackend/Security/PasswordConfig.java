package com.daribear.PrefyBackend.Security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Class to encode a user's password.
 */
@Configuration
public class PasswordConfig {

    /**
     * Encode password with specific strength.
     *
     * @return the encoded password
     */
    @Bean
    public PasswordEncoder PasswordEncoder(){
        return new BCryptPasswordEncoder(10);
    }
}
