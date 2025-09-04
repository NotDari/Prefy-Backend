package com.daribear.PrefyBackend.Authentication.Registration;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;

/**
 * The controller which handles the registration endpoints.
 */
@Controller
@RequestMapping(path = "prefy/v1/Registration")
@AllArgsConstructor
public class RegistrationController {

    private RegistrationService registrationService;

    /**
     * Registers a new user to the database
     * @param request
     * @return
     */
    @PostMapping
    @ResponseBody
    public String register(@RequestBody RegistrationRequest request){
        return registrationService.register(request);
    }

    @GetMapping(path = "Confirm")
    public String confirmRegistration(@RequestParam("token")String token){
        return registrationService.confirmToken(token);
    }

    @GetMapping(path = "ResendConfirmation")
    @ResponseBody
    public String resendConfirmationToken(@RequestParam("login")String login){
        return registrationService.resendToken(login);
    }

    @GetMapping(path = "/UsernameAvailable")
    @ResponseBody
    public Boolean checkIfUsernameAvailable(@RequestParam("username") String username){
        return registrationService.usernameAvailable(username);
    }

}
