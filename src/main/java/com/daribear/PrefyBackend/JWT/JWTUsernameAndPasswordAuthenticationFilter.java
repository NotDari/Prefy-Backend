package com.daribear.PrefyBackend.JWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.daribear.PrefyBackend.Errors.CustomError;
import com.daribear.PrefyBackend.Errors.CustomErrorObjectMapper;
import com.daribear.PrefyBackend.Errors.ErrorSchema;
import com.daribear.PrefyBackend.Errors.ErrorStorage;
import com.daribear.PrefyBackend.Users.User;
import com.daribear.PrefyBackend.Users.UserService;
import com.daribear.PrefyBackend.Utils.IntegrityApi.IntegrityHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

@AllArgsConstructor
public class JWTUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private JWTConfig jwtConfig;
    private UserService userService;
    private JWTService jwtService;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UsernameAndPasswordAuthenticationRequest authRequest = new ObjectMapper().readValue(request.getInputStream(), UsernameAndPasswordAuthenticationRequest.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(),
                    authRequest.getPassword()
            );
            Authentication authenticate = authenticationManager.authenticate(authentication);

            com.daribear.PrefyBackend.Authentication.Authentication details = (com.daribear.PrefyBackend.Authentication.Authentication) authenticate.getPrincipal();
            if (details.getLocked()){
                CustomError customError = new CustomError(HttpServletResponse.SC_UNAUTHORIZED, "Locked", 1);
                CustomErrorObjectMapper.getResponse(response, customError);
                return null;
            }

            (new IntegrityHelper()).validateToken(authRequest.getToken());
            return authenticate;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecretKey());
            Iterator<? extends GrantedAuthority> it = authResult.getAuthorities().iterator();
            ArrayList<String> authList = new ArrayList<>();
            for (int i =0; i < authResult.getAuthorities().size(); i++){
                authList.add(it.next().toString());
            }
            Optional<User> userOpt = userService.findById(((com.daribear.PrefyBackend.Authentication.Authentication)authResult.getPrincipal()).getId());
            if (userOpt.isPresent()) {
                ObjectMapper objectMapper = new ObjectMapper();
                String json = objectMapper.writeValueAsString(userOpt.get());
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
                response.getWriter().write(json);

                String token = JWT.create()
                        .withSubject(authResult.getName())
                        .withIssuedAt(new java.util.Date())
                        .withClaim("authorities", authList)
                        .withExpiresAt(Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
                        .sign(algorithm);
                response.addHeader("Authorization", jwtConfig.getTokenPrefix() + token);
                JWTClass jwtClass = new JWTClass();
                jwtClass.setAuthentication((com.daribear.PrefyBackend.Authentication.Authentication)authResult.getPrincipal());
                jwtClass.setToken(token);
                jwtClass.setIssueDate(LocalDateTime.now());
                jwtClass.setExpiryDate(LocalDateTime.now().plusDays(jwtConfig.getTokenExpirationAfterDays()));
                jwtService.saveToken(jwtClass);
            } else {
                CustomError customError = ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.InternalError);
                ErrorSchema errorSchema = new ErrorSchema(customError.getMessage(), customError.getCustomCode());
                ObjectMapper objectMapper = new ObjectMapper();
                String json = objectMapper.writeValueAsString(errorSchema);
                response.setStatus(customError.getHttpStatus());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
                response.getWriter().write(json);
            }

        } catch (JWTCreationException exception){
            //Invalid Signing configuration / Couldn't convert Claims.
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        CustomError customError;
        if (failed instanceof LockedException){
            customError = ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.UserAccountLocked);
        } else if (failed instanceof DisabledException){
            customError = ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.UserAccountDisabled);
        } else if (failed instanceof BadCredentialsException || failed instanceof UsernameNotFoundException){
            customError = ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.UserDetailsIncorrect);
        }else if (failed instanceof InternalAuthenticationServiceException) {
            if (failed.getCause() instanceof BadCredentialsException){
                customError = ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.UserDetailsIncorrect);
            } else {
                customError = ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.UnknownError);
            }
        }else {
            customError = ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.UnknownError);
        }
        ErrorSchema errorSchema = new ErrorSchema(customError.getMessage(), customError.getCustomCode());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(errorSchema);
        response.setStatus(customError.getHttpStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.getWriter().write(json);

    }


}
