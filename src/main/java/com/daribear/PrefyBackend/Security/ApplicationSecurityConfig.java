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
                .antMatchers("/actuator/**").permitAll()
                //.antMatchers("/actuator/**").hasRole("Admin")
                .anyRequest()
                .authenticated()
                ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)  {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        //CustomDaoAuthenticationHandler provider = new CustomDaoAuthenticationHandler();
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordConfig.PasswordEncoder());
        provider.setUserDetailsService(authService);
        return provider;
    }





}
