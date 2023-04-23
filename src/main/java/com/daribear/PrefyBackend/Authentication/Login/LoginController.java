package com.daribear.PrefyBackend.Authentication.Login;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.daribear.PrefyBackend.Authentication.AuthenticationService;
import com.daribear.PrefyBackend.Authentication.PasswordReset.PasswordResetService;
import com.daribear.PrefyBackend.Authentication.PasswordReset.PasswordSecurity;
import com.daribear.PrefyBackend.Errors.CustomError;
import com.daribear.PrefyBackend.Errors.ErrorStorage;
import com.daribear.PrefyBackend.JWT.JWTActions;
import com.daribear.PrefyBackend.JWT.JWTConfig;
import com.daribear.PrefyBackend.JWT.JWTService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(path = "prefy/v1/Login")
@AllArgsConstructor
public class LoginController {
    private final PasswordResetService passwordResetService;
    private final PasswordSecurity passwordSecurity;
    private final AuthenticationService authenticationService;
    private JWTService jwtService;
    private JWTConfig jwtConfig;


    @PostMapping("/ResetPassword")
    @PermitAll
    @ResponseBody
    public String resetPassword(HttpServletRequest request,
                                         @RequestParam("login") String userEmail) {
        return passwordResetService.sendPasswordReset(request ,userEmail);
    }

    @GetMapping("/UpdatePassword")
    @PermitAll
    public String showChangePasswordPage(@RequestParam(value = "token") String token, Model model){
        String result = passwordSecurity.validatePasswordResetToken(token);
        model.addAttribute("token", token);

        if (result != null) {
            model.addAttribute("message", "Invalid Token");
            return "TokenInvalidFile";
        }
        model.addAttribute("PasswordsMatch", "");

        return "passwordResetForm";
    }

    @PostMapping("/UpdatePassword")
    @PermitAll
    public String formSubmitted(HttpServletRequest request, Model model){
        //TODO Currently, due to not having ide Ultimate edition, the javascript for the password reset doesn't work
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmpassword");
        String token = request.getParameter("token");
        if (!confirmPassword.equals(password)){
            model.addAttribute("PasswordsMatch", "Passwords do not match");
            model.addAttribute("token", token);
            return "passwordResetForm";
        }
        String result = passwordSecurity.validatePasswordResetToken(token);
        if (result != null){
            return "TokenInvalidFile";
        }
        if (password == null){
            return "Error404";
        }else {
            Long id = passwordResetService.getAuthIdFromToken(token);
            if (id != null){
                updateTokenANDPassword(id, token, password);
            }else {
                return "Error404";
            }
        }
        return "ResetPasswordMessage";
    }

    @GetMapping("/Logout")
    @ResponseBody
    private String Logout(HttpServletRequest request){
        DecodedJWT jwt = JWTActions.getJWT(jwtConfig, request.getHeader("Authorization"));
        jwtService.logout(jwt);
        return (" Login:" + jwt.getSubject() + "  tokenCreationDate:" +jwt.getIssuedAt());
    }

    @GetMapping("/GetEmail")
    @ResponseBody
    private String GetEmail(HttpServletRequest request){
        DecodedJWT jwt = JWTActions.getJWT(jwtConfig, request.getHeader("Authorization"));
        if (jwt != null){
            return (" { \"email\" :"  + jwt.getSubject() + "}");
        } else {
            CustomError customError = ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.UserDetailsIncorrect);
            throw customError;
        }

    }


    @Transactional
    private void updateTokenANDPassword(Long id,String token, String password){
        authenticationService.alterPassword(id, password);
        passwordResetService.setPasswordTokenConfirmed(token);
    }








}
