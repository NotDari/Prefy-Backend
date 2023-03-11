package com.daribear.PrefyBackend.JWT;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.daribear.PrefyBackend.Authentication.AuthenticationService;
import com.daribear.PrefyBackend.Errors.CustomError;
import com.daribear.PrefyBackend.Errors.CustomErrorObjectMapper;
import com.daribear.PrefyBackend.Errors.ErrorStorage;
import com.daribear.PrefyBackend.Errors.ErrorStorage.ErrorType;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class JwtTokenVerifier extends OncePerRequestFilter {
    private JWTConfig jwtConfig;

    private JWTService jwtService;

    private AuthenticationService authService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Boolean denied = false;
        String authorizationheader = request.getHeader("Authorization");
        if (Strings.isNullOrEmpty(authorizationheader) || !authorizationheader.startsWith("Bearer")){
            filterChain.doFilter(request, response);
            return;
        }

        try {
            DecodedJWT jwt = JWTActions.getJWT(jwtConfig, authorizationheader);
            String username = jwt.getSubject();
            List<SimpleGrantedAuthority> authoritySet = jwt.getClaim("authorities").asList(SimpleGrantedAuthority.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    authoritySet
            );
            if (jwtService.tokenExists(jwt.getToken())){
                denied = true;
                createCustomError(response, ErrorType.UserLoggedOut);
            }
             if (AccountSecurityChecks(username, response, authoritySet, jwt)){
                 denied = true;
             }
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JWTVerificationException exception){
            throw new IllegalStateException(exception);
        }
        if (!denied) {
            filterChain.doFilter(request, response);
        }
    }

    private Boolean AccountSecurityChecks(String username, HttpServletResponse response, List<SimpleGrantedAuthority> authoritySet, DecodedJWT jwt){
        if (authService != null){
            Optional<com.daribear.PrefyBackend.Authentication.Authentication> authOpt = authService.getUserByEmail(username);
            if (authOpt.isPresent()){
                com.daribear.PrefyBackend.Authentication.Authentication auth = authOpt.get();
                HashSet<SimpleGrantedAuthority> authorityHashSet = new HashSet<>(authoritySet);
                if (auth.getLocked()){
                    createCustomError(response, ErrorType.UserAccountLocked);
                    return true;
                }else if (!auth.getEnabled()){
                    createCustomError(response, ErrorType.UserAccountDisabled);
                    return true;
                } else if (!auth.getGrantedAuthorities().equals(authorityHashSet)){
                    createCustomError(response, ErrorType.UserLoggedOut);
                    invalidateJWT(jwt);
                    return true;
                } else {
                    return false;
                }
            } else {
                createCustomError(response,ErrorStorage.ErrorType.UserDetailsIncorrect);
                return true;
            }
        }else {
            createCustomError(response,ErrorStorage.ErrorType.InternalError);
            return true;
        }
    }

    private void invalidateJWT(DecodedJWT jwt){
        jwtService.logout(jwt);
    }


    private void createCustomError(HttpServletResponse response, ErrorStorage.ErrorType errorType){
        CustomError customError = ErrorStorage.getCustomErrorFromType(errorType);
        CustomErrorObjectMapper.getResponse(response, customError);
    }



}
