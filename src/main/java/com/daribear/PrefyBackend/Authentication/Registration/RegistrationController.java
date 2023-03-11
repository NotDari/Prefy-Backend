package com.daribear.PrefyBackend.Authentication.Registration;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;

@RestController
@RequestMapping(path = "prefy/v1/Registration")
@AllArgsConstructor
public class RegistrationController {

    private RegistrationService registrationService;

    @PostMapping
    public String register(@RequestBody RegistrationRequest request){
        System.out.println("SdAD ADADS");
        return registrationService.register(request);
    }

    @GetMapping(path = "Confirm")
    public String confirmRegistration(@RequestParam("token")String token){
        return registrationService.confirmToken(token);
    }

    @GetMapping(path = "ResendConfirmation")
    public String resendConfirmationToken(@RequestParam("login")String login){
        return registrationService.resendToken(login);
    }

    @GetMapping(path = "/UsernameAvailable")
    public Boolean checkIfUsernameAvailable(@RequestParam("username") String username){
        return registrationService.usernameAvailable(username);
    }

}
