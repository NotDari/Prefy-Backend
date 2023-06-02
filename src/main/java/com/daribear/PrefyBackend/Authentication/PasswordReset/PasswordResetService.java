package com.daribear.PrefyBackend.Authentication.PasswordReset;

import com.daribear.PrefyBackend.Authentication.Authentication;
import com.daribear.PrefyBackend.Authentication.AuthenticationService;
import com.daribear.PrefyBackend.Email.EMAILFORMATS;
import com.daribear.PrefyBackend.Email.EmailSender;
import com.daribear.PrefyBackend.Errors.ErrorStorage;
import com.daribear.PrefyBackend.Users.User;
import com.daribear.PrefyBackend.Users.UserService;
import com.daribear.PrefyBackend.Utils.ServerAddress;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PasswordResetService {
    private final AuthenticationService authService;
    private final UserService userService;
    private final EmailSender emailSender;
    private final PasswordTokenRepository passwordTokenRepository;
    private Environment environment;

    public String sendPasswordReset(String login){
        Optional<Authentication> optAuth;
        if (login.contains("@")) {
            optAuth = authService.getUserByEmail(login);
        } else {
            Optional<User> optUser = userService.findByUsername(login);
            if (optUser.isPresent()){
                optAuth = authService.getUserById(optUser.get().getId());
            } else {
                throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.USERNOTFOUND);
            }
        }
        if (optAuth.isEmpty()) {
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.USERNOTFOUND);
        }
        Authentication auth = optAuth.get();
        String token = UUID.randomUUID().toString();
        createPasswordResetTokenForUser(auth, token);
        emailSender.send(auth.getEmail(), "Prefy Reset Password", EMAILFORMATS.PasswordReset("nobody", (ServerAddress.getServerAddress() + "/prefy/v1/Login/UpdatePassword?token="+token)));
        return "sent";
    }

    public void createPasswordResetTokenForUser(Authentication authentication, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, authentication, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15));
        passwordTokenRepository.save(myToken);
    }

    public Long getAuthIdFromToken(String token){
        return passwordTokenRepository.getIdByToken(token).getId();
    }

    public int setPasswordTokenConfirmed(String token){
        return passwordTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }




}
