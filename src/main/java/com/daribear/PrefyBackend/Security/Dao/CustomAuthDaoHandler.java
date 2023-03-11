package com.daribear.PrefyBackend.Security.Dao;

import com.daribear.PrefyBackend.Authentication.AuthenticationRepository;
import com.daribear.PrefyBackend.Errors.CustomError;
import com.daribear.PrefyBackend.Security.PasswordConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Component
public class CustomAuthDaoHandler {

    @Autowired
    private AuthenticationRepository authRepo;

    public UsernamePasswordAuthenticationToken authenticate (Authentication authentication) {
        System.out.println("Sdad aa");
        String username = authentication.getPrincipal() + "";
        String password = authentication.getCredentials() + "";

        Optional<com.daribear.PrefyBackend.Authentication.Authentication> user =  authRepo.findByEmail(username);
        if (user.isEmpty()) {
            throw new CustomError(HttpServletResponse.SC_UNAUTHORIZED, "Couldn't find a user with that login", 1);
        }
        PasswordConfig passwordConfig = new PasswordConfig();

        if (passwordConfig.PasswordEncoder().matches(password, user.get().getPassword())){
            throw new CustomError(HttpServletResponse.SC_UNAUTHORIZED, "Password incorrect", 2);
        }
        if (!user.get().isEnabled()) {
            throw new CustomError(HttpServletResponse.SC_UNAUTHORIZED, "Account locked", 4);
        }
        List<SimpleGrantedAuthority> userRights = (List<SimpleGrantedAuthority>) user.get().getGrantedAuthorities();
        return new UsernamePasswordAuthenticationToken(username, null, userRights);
    }
}
