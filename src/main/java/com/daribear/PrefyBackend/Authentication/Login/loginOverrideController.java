package com.daribear.PrefyBackend.Authentication.Login;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(path = "")
@AllArgsConstructor
public class loginOverrideController {

    @PostMapping("/login")
    @PermitAll
    @ResponseBody
    public String resetPassword() {
        System.out.println("Sdad hiya!");
        return "login";
    }
}
