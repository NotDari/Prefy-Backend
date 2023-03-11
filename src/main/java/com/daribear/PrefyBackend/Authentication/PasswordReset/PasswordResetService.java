package com.daribear.PrefyBackend.Authentication.PasswordReset;

import com.daribear.PrefyBackend.Authentication.Authentication;
import com.daribear.PrefyBackend.Authentication.AuthenticationService;
import com.daribear.PrefyBackend.Email.EMAILFORMATS;
import com.daribear.PrefyBackend.Email.EmailSender;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PasswordResetService {
    private final AuthenticationService authService;
    private final EmailSender emailSender;
    private final PasswordTokenRepository passwordTokenRepository;

    public String sendPasswordResetEmail(HttpServletRequest request, String email){
        Optional<Authentication> optAuth = authService.getUserByEmail(email);
        if (optAuth.isEmpty()) {
            throw new UsernameNotFoundException(String.format("User with Email Not Found", email));
        }
        Authentication auth = authService.getUserByEmail(email).get();
        String token = UUID.randomUUID().toString();
        createPasswordResetTokenForUser(auth, token);
        emailSender.send(email, "Prefy Reset Password", EMAILFORMATS.PasswordReset("nobody", ("http://localhost:8080/prefy/v1/Login/UpdatePassword?token="+token)));
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
