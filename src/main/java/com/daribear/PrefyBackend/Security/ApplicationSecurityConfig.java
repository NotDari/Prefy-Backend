package com.daribear.PrefyBackend.Security;

import com.daribear.PrefyBackend.Authentication.AuthenticationService;
import com.daribear.PrefyBackend.Security.AuthenticationHandlers.CustomAccessDeniedHandler;
import com.daribear.PrefyBackend.JWT.JWTConfig;
import com.daribear.PrefyBackend.JWT.JWTService;
import com.daribear.PrefyBackend.JWT.JWTUsernameAndPasswordAuthenticationFilter;
import com.daribear.PrefyBackend.JWT.JwtTokenVerifier;
import com.daribear.PrefyBackend.Security.AuthenticationHandlers.CustomAuthenticationEntryPoint;
import com.daribear.PrefyBackend.Security.Dao.CustomDaoAuthenticationHandler;
import com.daribear.PrefyBackend.Users.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


/**
 * This is my Security Configuration class, which sets up the rules as well as authentication for my project. It controls
 * which endpoints require authorisation and which are available to the public.
 * It also partially dictates which endpoints require admins.
 * Finally it sets up my custom dao authentication provider.
 *
 */
@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
    private final AuthenticationService authService;
    private PasswordConfig passwordConfig;

    private CustomAccessDeniedHandler accessDeniedHandler;
    private CustomAuthenticationEntryPoint authenticationEntryPoint;

    private JWTConfig jwtConfig;

    private JWTService jwtService;
    private UserService userService;


    /**
     * Overrides the spring configure.
     * Sets my custom authentication entry point, and access denied handler
     * and adds a jwt security filter for login and request verification.
     *
     * It also forces all requests to be authorised unless explicitly stated otherwise.
     * In this instance login and registration don't necessarily need authorisation.
     *
     *
     * Finally it enforces requests to use the bot or admin features to require an authenticated user with an admin role.
     *
     * @param http the http request
     * @throws Exception if an error occurs
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JWTUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, userService, jwtService))
                .addFilterAfter(new JwtTokenVerifier(jwtConfig, jwtService, authService), JWTUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/prefy/v1/Login/**").permitAll()
                .antMatchers("/prefy/v1/Registration/**").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/actuator/**").hasRole("Admin")
                .antMatchers("/prefy/v1/Admin/**").hasRole("Admin")
                .antMatchers("prefy/v1/Spongebot").hasRole("Admin")
                .anyRequest()
                .authenticated();
    }

    /**
     * Overrides the default authentication manager to use my custom authenticationProvider.
     * @param auth the AuthenticationManagerBuilder
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth)  {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    /**
     * Overrides the custom daoAuthenticationProvider to use my custum password config and userDetails Service
     * @return the new daoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        //CustomDaoAuthenticationHandler provider = new CustomDaoAuthenticationHandler();
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordConfig.PasswordEncoder());
        provider.setUserDetailsService(authService);
        return provider;
    }





}
