package com.daribear.PrefyBackend.Authentication;

import com.daribear.PrefyBackend.Authentication.Registration.RegistrationRequest;
import com.daribear.PrefyBackend.Authentication.Registration.RegistrationToken.RegistrationConfirmationToken;
import com.daribear.PrefyBackend.Authentication.Registration.RegistrationToken.RegistrationConfirmationTokenService;
import com.daribear.PrefyBackend.Email.EMAILFORMATS;
import com.daribear.PrefyBackend.Email.EmailSender;
import com.daribear.PrefyBackend.Errors.CustomError;
import com.daribear.PrefyBackend.Errors.ErrorStorage;
import com.daribear.PrefyBackend.Security.PasswordConfig;
import com.daribear.PrefyBackend.UserInfo.UserInfo;
import com.daribear.PrefyBackend.UserInfo.UserInfoService;
import com.daribear.PrefyBackend.Users.User;
import com.daribear.PrefyBackend.Users.UserService;
import com.daribear.PrefyBackend.Utils.ComputerIp;
import com.daribear.PrefyBackend.Utils.CurrentTime;
import com.daribear.PrefyBackend.Utils.ServerAddress;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
@AllArgsConstructor
public class AuthenticationService implements UserDetailsService {
    private final AuthenticationRepository authRepository;
    private final UserService userService;
    private final UserInfoService userInfoService;
    private final EmailSender emailSender;
    private final RegistrationConfirmationTokenService registrationConfirmationTokenService;
    private PasswordConfig passwordConfig;


    @Override
    public UserDetails loadUserByUsername(String username) throws CustomError {
        Optional<Authentication> emailAuth = authRepository.findByEmail(username);
        Optional<User> Id = userService.findByUsername(username);
        if (emailAuth.isEmpty() && Id.isEmpty()){
            //customErrorService.setCustomCode(HttpServletResponse.SC_UNAUTHORIZED);
           // customErrorService.setMessage("failed");
           // customErrorService.setCustomCode(1);
            throw new BadCredentialsException("Bad credentials");
            //throw new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, username));
        }else if (Id.isPresent()){
            Optional<Authentication> UserNameAuth = authRepository.findById(Id.get().getId());
            if (UserNameAuth.isPresent()){
                return UserNameAuth.get();
            } else {
              //  customErrorService.setCustomCode(HttpServletResponse.SC_UNAUTHORIZED);
               // customErrorService.setMessage("failed");
               // customErrorService.setCustomCode(1);
                throw new CustomError(HttpServletResponse.SC_UNAUTHORIZED, "failed", 1);
                //throw new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, username));
            }
        }


        return emailAuth.get();
    }

    public Optional<Authentication> getUserByEmail(String email){

        return authRepository.findByEmail(email);
    }

    public Optional<Authentication> getUserById(Long id){
        return authRepository.findById(id);
    }

    public String signUpUser(Authentication authentication, RegistrationRequest registrationRequest){
        String username = registrationRequest.getUsername();
        String fullName = registrationRequest.getFullname();
        boolean usernameTaken = userService.userNameExists(username);
        if (usernameTaken){
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.REGEUSERTAKE);
            //throw new IllegalStateException("username already taken");
        }
        boolean emailTaken = authRepository.findByEmail(authentication.getEmail()).isPresent();
        if (emailTaken){
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.REGEMAILTAKE);
        }

        String encodedPassword = passwordConfig.PasswordEncoder().encode(authentication.getPassword());
        authentication.setPassword(encodedPassword);

        authRepository.save(authentication);
        String token = UUID.randomUUID().toString();
        RegistrationConfirmationToken registrationConfirmationToken = new RegistrationConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                authentication
        );
        registrationConfirmationTokenService.saveConfirmationToken(registrationConfirmationToken);
        String systemipaddress = ComputerIp.getComputerAddress();
        emailSender.send(authentication.getEmail(),"Prefy Confirm Email", EMAILFORMATS.RegistrationConfirmation(username, ServerAddress.getServerAddress() + "/prefy/v1/Registration/Confirm?token=" + token));
        User user = new User(authentication.getId(), username, "none", fullName, 0L, 0L, 0L,0L, 0L, "", "", "", "", false, false, null);
        userService.addNewUser(user);
        UserInfo userInfo = new UserInfo(authentication, ((Long) CurrentTime.getCurrentTime()).doubleValue(), ((Long)registrationRequest.getDOB().getTime()).doubleValue());
        userInfoService.saveUserInfo(userInfo);
        return "success";
    }

    public int alterPassword(Long id, String newPassword){
        String encodedPassword = passwordConfig.PasswordEncoder().encode(newPassword);
        return authRepository.alterPassword(id, encodedPassword);
    }




    public int enableAuthentication(String email) {
        return authRepository.enableAuthentication(email);
    }

}
