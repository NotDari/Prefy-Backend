package com.daribear.PrefyBackend.Authentication.Registration;


import com.daribear.PrefyBackend.Authentication.Authentication;
import com.daribear.PrefyBackend.Authentication.AuthenticationService;
import com.daribear.PrefyBackend.Authentication.Registration.RegistrationToken.RegistrationConfirmationToken;
import com.daribear.PrefyBackend.Authentication.Registration.RegistrationToken.RegistrationConfirmationTokenService;
import com.daribear.PrefyBackend.Email.EMAILFORMATS;
import com.daribear.PrefyBackend.Email.EmailSender;
import com.daribear.PrefyBackend.Security.ApplicationUserRole;
import com.daribear.PrefyBackend.Users.User;
import com.daribear.PrefyBackend.Users.UserService;
import com.daribear.PrefyBackend.Utils.ComputerIp;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static com.daribear.PrefyBackend.Security.ApplicationUserRole.*;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final AuthenticationService authenticationService;
    private final EmailValidator emailValidator;
    private final EmailSender emailSender;
    private final UserService userService;
    private RegistrationConfirmationTokenService registrationConfirmationTokenService;

    public String register(RegistrationRequest request) {
        boolean isEmailValid = emailValidator.test(request.getEmail());
        if (!isEmailValid){
            throw new IllegalStateException("email not valid");
        }
        Authentication newUser = new Authentication(request.getEmail(), request.getPassword(), User.getGrantedAuthorities());
        System.out.println("Sdad userAuthorities:" + newUser.getGrantedAuthorities());
        newUser.setLocked(false);
        newUser.setEnabled(false);
        return authenticationService.signUpUser(newUser, request.getUsername(), request.getFullname());
    }


    @Transactional
    public String confirmToken(String token) {
        RegistrationConfirmationToken confirmationToken = registrationConfirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiredAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        registrationConfirmationTokenService.setConfirmedAt(token);
        authenticationService.enableAuthentication(
                confirmationToken.getAuthentication().getEmail());
        return "confirmed";
    }

    public String resendToken(String login){
        String email;
        String username;
        Optional<Authentication> auth;
        System.out.println("Sdad login:" + login);
        if (login.contains("@")){
            email = login;
            auth = authenticationService.getUserByEmail(email);
            if (auth.isPresent()){
                Long id = auth.get().getId();
                Optional<User> user = userService.findById(id);
                if (user.isPresent()){
                    username = user.get().getUsername();
                } else {
                    return "Error Account doesn't exist";
                }
            } else {
                return "Error Account doesn't exist";
            }
        } else{
            username = login;
            Optional<User> user = userService.findByUsername(username);
            if (user.isPresent()){
                Long id = user.get().getId();
                auth = authenticationService.getUserById(id);
                if (auth.isPresent()){
                    email = auth.get().getEmail();
                }else {
                    return "Error Account doesn't exist";
                }
            } else {
                return "Error Account doesn't exist";
            }
        }
        System.out.println("Sdad emailToken AUTH PRESENT? : " + auth.isPresent());
        if (auth.isPresent()){
            System.out.println("Sdad emailToken AUTH PRESENT");
            if (!auth.get().getEnabled()){
                Optional<RegistrationConfirmationToken> oldToken = registrationConfirmationTokenService.getTokenByAuthId(auth.get().getId());
                if (oldToken.isPresent()){
                    if (oldToken.get().getConfirmedAt() != null){
                        authenticationService.enableAuthentication(email);
                        return "Account already enabled";
                    } else {
                        if (oldToken.get().getExpiredAt().isAfter(LocalDateTime.now())){
                            registrationConfirmationTokenService.invalidateToken(oldToken.get().getToken());
                        }
                        String token = UUID.randomUUID().toString();
                        RegistrationConfirmationToken registrationConfirmationToken = new RegistrationConfirmationToken(
                                token,
                                LocalDateTime.now(),
                                LocalDateTime.now().plusMinutes(15),
                                auth.get()
                        );
                        registrationConfirmationTokenService.resendToken(registrationConfirmationToken);
                        String systemipaddress = ComputerIp.getComputerAddress();
                        emailSender.send(auth.get().getEmail(),"Prefy Confirm Email", EMAILFORMATS.RegistrationConfirmation(username, "http:/" + systemipaddress + ":8090/prefy/v1/Registration/Confirm?token=" + token));

                        return "Token Resent";
                    }
                } else {
                    String token = UUID.randomUUID().toString();
                    RegistrationConfirmationToken registrationConfirmationToken = new RegistrationConfirmationToken(
                            token,
                            LocalDateTime.now(),
                            LocalDateTime.now().plusMinutes(15),
                            auth.get()
                    );
                    registrationConfirmationTokenService.saveConfirmationToken(registrationConfirmationToken);

                    //TODO This is only for the system IP address
                    String systemipaddress = ComputerIp.getComputerAddress();
                    emailSender.send(auth.get().getEmail(),"Prefy Confirm Email", EMAILFORMATS.RegistrationConfirmation(username, "http:/" + systemipaddress + ":8090/prefy/v1/Registration/Confirm?token=" + token));
                    return "Token Resent";
                }
            } else {
                return "Acccount already enabled";
            }
        } else {
            return "Error Account doesn't exist";
        }
    }

    public Boolean usernameAvailable(String username){
        return !userService.userNameExists(username);
    }

}
