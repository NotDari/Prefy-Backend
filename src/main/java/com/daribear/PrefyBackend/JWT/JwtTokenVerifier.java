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

/**
 * Part of my seccurity filter that allows for the validation of JWT token to authorise user logging in without
 * them having to provide their username and password.
 */
@AllArgsConstructor
public class JwtTokenVerifier extends OncePerRequestFilter {
    private JWTConfig jwtConfig;

    private JWTService jwtService;

    private AuthenticationService authService;

    /**
     * Applies an internal filter to allow the user to utilise a JWT token instead of having to relog in with their username and password.
     * Checks if there is a valid jwt, that isn't yet invalid.
     * Also checks if user account is still valid and ok to log in.
     * If everything ok, allow them else bans them.
     *
     * @param request HTTP request provided
     * @param response HTTP Response to write into
     * @param filterChain the springboot filter chain
     * @throws ServletException if there is an issue with my filter
     * @throws IOException if an IO exception occurs during request or response processing
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Boolean denied = false;
        String authorizationheader = request.getHeader("Authorization");
        //JWT token isn't right format or doesn't exist so check next filter
        if (Strings.isNullOrEmpty(authorizationheader) || !authorizationheader.startsWith("Bearer")){
            filterChain.doFilter(request, response);
            return;
        }

        try {
            //Get the jwt token and get the permissions
            DecodedJWT jwt = JWTActions.getJWT(jwtConfig, authorizationheader);
            String username = jwt.getSubject();
            List<SimpleGrantedAuthority> authoritySet = jwt.getClaim("authorities").asList(SimpleGrantedAuthority.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    authoritySet
            );
            //Check JWT token valid and active
            if (!jwtService.tokenExists(jwt.getToken())){
                denied = true;
                createCustomError(response, ErrorType.UserLoggedOut);
            }
            if (!jwtService.tokenValid(jwt.getToken())){
                denied = true;
                createCustomError(response, ErrorType.UserLoggedOut);
            }
            //Check if issue with account
             if (AccountSecurityChecks(username, response, authoritySet, jwt)){
                 denied = true;
             }
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JWTVerificationException exception){
            throw new IllegalStateException(exception);
        }
        //User isn't denied so continue with other filters to check
        if (!denied) {
            filterChain.doFilter(request, response);
        }
    }

    /**
     * Performs checks on account such as the account is banned or not, or if their permissions are the same level to the JWT token.
     *
     *
     * @param username account username
     * @param response response to right into
     * @param authoritySet user's permissions
     * @param jwt jwt token
     * @return boolean whether the account is invalid(true) or valid (false)
     */
    private Boolean AccountSecurityChecks(String username, HttpServletResponse response, List<SimpleGrantedAuthority> authoritySet, DecodedJWT jwt){
        if (authService != null){
            Optional<com.daribear.PrefyBackend.Authentication.Authentication> authOpt = authService.getUserByEmail(username);
            if (authOpt.isPresent()){
                //Get user permissions
                com.daribear.PrefyBackend.Authentication.Authentication auth = authOpt.get();
                HashSet<SimpleGrantedAuthority> authorityHashSet = new HashSet<>(authoritySet);
                //Account locked
                if (auth.getLocked()){
                    createCustomError(response, ErrorType.UserAccountLocked);
                    return true;
                }
                //Account not enabled
                else if (!auth.getEnabled()){
                    createCustomError(response, ErrorType.UserAccountDisabled);
                    return true;
                }
                //Invalid JWT token if mismatch in permissions
                else if (!auth.getGrantedAuthorities().equals(authorityHashSet)){
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

    /**
     * Invalidate the jwt token
     * @param jwt the token to invalidate
     */
    private void invalidateJWT(DecodedJWT jwt){
        jwtService.logout(jwt);
    }

    /**
     * Creates a cuistom error
     * @param response response to write into
     * @param errorType type of error to create
     */
    private void createCustomError(HttpServletResponse response, ErrorStorage.ErrorType errorType){
        CustomError customError = ErrorStorage.getCustomErrorFromType(errorType);
        CustomErrorObjectMapper.getResponse(response, customError);
    }



}
