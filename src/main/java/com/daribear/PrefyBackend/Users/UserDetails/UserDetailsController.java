package com.daribear.PrefyBackend.Users.UserDetails;

import com.daribear.PrefyBackend.Authentication.Authentication;
import com.daribear.PrefyBackend.Authentication.AuthenticationService;
import com.daribear.PrefyBackend.Errors.CustomError;
import com.daribear.PrefyBackend.Errors.ErrorStorage;
import com.daribear.PrefyBackend.IncomeClasses.IncomeUserIdList;
import com.daribear.PrefyBackend.Suggestions.Suggestion;
import com.daribear.PrefyBackend.Users.User;
import com.daribear.PrefyBackend.Users.UserDetails.DetailsReturnClasses.UsernameUpdaterReturn;
import com.daribear.PrefyBackend.Users.UserService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "prefy/v1/Users/UserDetails")
@AllArgsConstructor
public class UserDetailsController {
    private UserDetailsEditService userDetailsService;
    private AuthenticationService authService;

    @PostMapping("/UpdateUserImage")
    public void updateUserImage(Principal principal, @RequestBody Map<String, Object> requestBody){
        if (principal == null){
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.InternalError);
        }
        Optional<Authentication> optionalUser = authService.getUserByEmail(principal.getName());
        if (optionalUser.isEmpty()){
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.InternalError);
        }

        userDetailsService.updateUserImage(optionalUser.get().getId(), (String) requestBody.get("imageURL"));
    }

    @PostMapping("/UpdateBio")
    public void updateBio(Principal principal, @RequestBody Map<String, Object> requestBody){
        if (principal == null){
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.InternalError);
        }
        Optional<Authentication> optionalUser = authService.getUserByEmail(principal.getName());
        if (optionalUser.isEmpty()){
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.InternalError);
        }

        userDetailsService.updateBio(optionalUser.get().getId(), (String) requestBody.get("bio"));
    }

    @PostMapping("/UpdateFullname")
    public void updateFullname(Principal principal, @RequestBody Map<String, Object> requestBody){
        if (principal == null){
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.InternalError);
        }
        Optional<Authentication> optionalUser = authService.getUserByEmail(principal.getName());
        if (optionalUser.isEmpty()){
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.InternalError);
        }

        userDetailsService.updateFullName(optionalUser.get().getId(), (String) requestBody.get("fullname"));
    }

    @PostMapping("/UpdateSocialMedia")
    public void updateSocialMedia(Principal principal, @RequestBody Map<String, Object> requestBody){
        if (principal == null){
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.InternalError);
        }
        Optional<Authentication> optionalUser = authService.getUserByEmail(principal.getName());
        if (optionalUser.isEmpty()){
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.InternalError);
        }

        userDetailsService.updateSocialMedia(optionalUser.get().getId(), (String) requestBody.get("type") ,(String) requestBody.get("value"));
    }



    @PostMapping("/UpdateUsername")
    public UsernameUpdaterReturn updateUsername(Principal principal, @RequestBody Map<String, Object> requestBody){
        if (principal == null){
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.InternalError);
        }
        Optional<Authentication> optionalUser = authService.getUserByEmail(principal.getName());
        if (optionalUser.isEmpty()){
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.InternalError);
        }
        UsernameUpdaterReturn usernameUpdaterReturn = new UsernameUpdaterReturn();
        usernameUpdaterReturn.setUsernameTaken(userDetailsService.updateUsername(optionalUser.get().getId(), (String) requestBody.get("username")));
        return usernameUpdaterReturn;
    }





}
