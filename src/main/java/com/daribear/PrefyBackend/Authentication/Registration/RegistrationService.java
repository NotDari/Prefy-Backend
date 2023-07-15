package com.daribear.PrefyBackend.Authentication.Registration;


import com.daribear.PrefyBackend.Authentication.Authentication;
import com.daribear.PrefyBackend.Authentication.AuthenticationService;
import com.daribear.PrefyBackend.Authentication.Registration.RegistrationToken.RegistrationConfirmationToken;
import com.daribear.PrefyBackend.Authentication.Registration.RegistrationToken.RegistrationConfirmationTokenService;
import com.daribear.PrefyBackend.Email.EMAILFORMATS;
import com.daribear.PrefyBackend.Email.EmailSender;
import com.daribear.PrefyBackend.Errors.CustomError;
import com.daribear.PrefyBackend.Errors.ErrorStorage;
import com.daribear.PrefyBackend.Security.ApplicationUserRole;
import com.daribear.PrefyBackend.Users.User;
import com.daribear.PrefyBackend.Users.UserService;
import com.daribear.PrefyBackend.Utils.ComputerIp;
import com.daribear.PrefyBackend.Utils.IntegrityApi.IntegrityHelper;
import com.daribear.PrefyBackend.Utils.ServerAddress;
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
        (new IntegrityHelper()).getToken(request.getToken());
        boolean isEmailValid = emailValidator.test(request.getEmail());
        if (!isEmailValid){
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.REGISTEREMAILNOTVALID);
        }
        if (authenticationService.getUserByEmail(request.getEmail()).isPresent()){
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.REGISTEREMAILTAKEN);
        }
        Authentication newUser = new Authentication(request.getEmail(), request.getPassword(), User.getGrantedAuthorities());
        newUser.setLocked(false);
        newUser.setEnabled(false);

        return authenticationService.signUpUser(newUser, request);
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
        return "email_confirmed";
    }

    public String resendToken(String login){
        String email;
        String username;
        Optional<Authentication> auth;
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
        if (auth.isPresent()){
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
                        emailSender.send(auth.get().getEmail(),"Prefy Confirm Email", EMAILFORMATS.RegistrationConfirmation(username, ServerAddress.getServerAddress() + "/prefy/v1/Registration/Confirm?token=" + token));

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


                    emailSender.send(auth.get().getEmail(),"Prefy Confirm Email", EMAILFORMATS.RegistrationConfirmation(username, ServerAddress.getServerAddress() + "/prefy/v1/Registration/Confirm?token=" + token));
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
