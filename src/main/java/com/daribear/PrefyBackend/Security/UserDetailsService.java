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

@AllArgsConstructor
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private AuthenticationRepository authRepo;
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Sdad HELLO" + username);
        Optional<Authentication> emailAuth = authRepo.findByEmail(username);
        Optional<User> Id = userRepo.findByUsernameIgnoreCase(username);
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
