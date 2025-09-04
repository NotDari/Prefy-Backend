package com.daribear.PrefyBackend.Authentication.Login;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;

/**
 *
 */
@Controller
@RequestMapping(path = "")
@AllArgsConstructor
public class loginOverrideController {

    @PostMapping("/login")
    @PermitAll
    @ResponseBody
    public String login(@RequestBody String token) {
        return "login";
    }


}
