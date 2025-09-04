package com.daribear.PrefyBackend.Authentication.Login;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.daribear.PrefyBackend.Authentication.AuthenticationService;
import com.daribear.PrefyBackend.Authentication.PasswordReset.PasswordResetService;
import com.daribear.PrefyBackend.Authentication.PasswordReset.PasswordSecurity;
import com.daribear.PrefyBackend.Authentication.Registration.RegistrationService;
import com.daribear.PrefyBackend.Errors.CustomError;
import com.daribear.PrefyBackend.Errors.ErrorStorage;
import com.daribear.PrefyBackend.JWT.JWTActions;
import com.daribear.PrefyBackend.JWT.JWTConfig;
import com.daribear.PrefyBackend.JWT.JWTService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;

/**
 * Controller for the handling of the endpoints of login.
 */
@Controller
@RequestMapping(path = "prefy/v1/Login")
@AllArgsConstructor
public class LoginController {
    private final PasswordResetService passwordResetService;
    private final PasswordSecurity passwordSecurity;
    private final AuthenticationService authenticationService;
    private JWTService jwtService;
    private JWTConfig jwtConfig;
    private RegistrationService registrationService;

    /**
     * Resets the user's password, which means that an email is sent to them to change it
     * @param userEmail userEmail to send the request to
     * @return the response
     */
    @PostMapping("/ResetPassword")
    @ResponseBody
    public String resetPassword(@RequestParam("login") String userEmail) {
        return passwordResetService.sendPasswordReset(userEmail);
    }

    /**
     * Displays the password reset form for a user's token.
     * @param token password reset token
     * @param model the spring model to pass to the html view
     * @return
     */
    @GetMapping("/UpdatePassword")
    public String showChangePasswordPage(@RequestParam(value = "token") String token, Model model){
        String result = passwordSecurity.validatePasswordResetToken(token);
        model.addAttribute("token", token);

        if (result != null) {
            model.addAttribute("message", "Invalid Token");
            return "TokenInvalidFile";
        }
        return "PasswordResetForm";
    }

    /**
     * This is used to update the password of a user, based on the submission of the password request form.
     * Returns a successful page or error page.
     *
     * @param request http request
     * @param model the spring model to pass to the html view
     * @return
     */
    @PostMapping("/UpdatePassword")
    public String formSubmitted(HttpServletRequest request, Model model){
        String password = request.getParameter("new-password");
        String token = request.getParameter("token");

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

    /**
     * Logout by changing the ban rate of the JWT token
     * @param request http request containing the token
     * @return Response to return to the client
     */
    @GetMapping("/Logout")
    @ResponseBody
    private String Logout(HttpServletRequest request){
        DecodedJWT jwt = JWTActions.getJWT(jwtConfig, request.getHeader("Authorization"));
        jwtService.logout(jwt);
        return (" Login:" + jwt.getSubject() + "  tokenCreationDate:" +jwt.getIssuedAt());
    }

    /**
     * Retrieves the email of the currently authenticated user(uses the JWT token to get this)
     * @param request HTTP request
     * @return json with the user's email
     */
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

    /**
     * Updates the user password and alter the token so that it has been used.
     * @param id user'id
     * @param token token to be altered
     * @param password new password
     */
    @Transactional
    private void updateTokenANDPassword(Long id,String token, String password){
        authenticationService.alterPassword(id, password);
        passwordResetService.setPasswordTokenConfirmed(token);
    }









}
