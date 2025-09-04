package com.daribear.PrefyBackend.Security;

import com.daribear.PrefyBackend.Authentication.Authentication;
import com.daribear.PrefyBackend.Authentication.AuthenticationRepository;
import com.daribear.PrefyBackend.Users.User;
import com.daribear.PrefyBackend.Users.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Overrides springboots UserDetailsService to allow for fetching of user details during login,
 * which allows for login via email or username.
 */
@AllArgsConstructor
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private AuthenticationRepository authRepo;
    private UserRepository userRepo;

    /**
     * Attempts to load the user details using both email and then username, and throws an error if neither works.
     * Otherwise returns the details, for authentication.
     *
     *
     * @param username the username/email required to log the user in.
     * @return the authentication details required(like password and roles)
     * @throws UsernameNotFoundException if no user exists
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Authentication> emailAuth = authRepo.findByEmail(username);
        Optional<User> Id = userRepo.findByUsername(username);
        if (emailAuth.isEmpty() && Id.isEmpty()){
            throw new UsernameNotFoundException("Not found!");
        }else if (Id.isPresent()){
            Optional<Authentication> UserNameAuth = authRepo.findById(Id.get().getId());
            if (UserNameAuth.isPresent()){
                return UserNameAuth.get();
            } else {
                throw new UsernameNotFoundException("Not found!");
            }
        }


        return emailAuth.get();
    }
}
