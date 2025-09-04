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

/**
 * The rest controller for handling the altering of the user's profile page details(stuff the user can set).
 */
@RestController
@RequestMapping(path = "prefy/v1/Users/UserDetails")
@AllArgsConstructor
public class UserDetailsController {
    private UserDetailsEditService userDetailsService;
    private AuthenticationService authService;

    /**
     * Attempts to Update the url image of the user's profile picture
     * @param principal the authenticated user making the request
     * @param requestBody The body of the request containing the new image url
     */
    @PostMapping("/UpdateUserImage")
    public void updateUserImage(Principal principal, @RequestBody Map<String, Object> requestBody){
        if (principal == null){
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.InternalError);
        }
        Optional<Authentication> optionalUser = authService.getUserByEmail(principal.getName());
        //Return error if couldn't find suer
        if (optionalUser.isEmpty()){
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.InternalError);
        }

        userDetailsService.updateUserImage(optionalUser.get().getId(), (String) requestBody.get("imageURL"));
    }

    /**
     * Attempts to update the bio of the user.
     *
     * @param principal the authenticated user making the request
     * @param requestBody The body of the request containing the new bio
     */
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

    /**
     * Attempts to update the full name of the user
     * @param principal the authenticated user making the request
     * @param requestBody The body of the request containing the new full name
     */
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


    /**
     * Attempts to update one of the social media handles of the user
     *
     * @param principal the authenticated user making the request
     * @param requestBody The body of the request containing the new full name
     */
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


    /**
     * Attempts to update the username, returning UsernameUpdaterReturn which tells the application if the username thats wanted
     * is already taken
     *
     * @param principal the authenticated user making the request
     * @param requestBody The body of the request containing the new username
     * @return a class containing a boolean which tells the application if the usrname is already taken
     */
    @PostMapping("/UpdateUsername")
    public UsernameUpdaterReturn updateUsername(Principal principal, @RequestBody Map<String, Object> requestBody){
        if (principal == null){
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.InternalError);
        }
        Optional<Authentication> optionalUser = authService.getUserByEmail(principal.getName());
        if (optionalUser.isEmpty()){
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.InternalError);
        }
        //Whether the username is already taken
        UsernameUpdaterReturn usernameUpdaterReturn = new UsernameUpdaterReturn();
        usernameUpdaterReturn.setUsernameTaken(userDetailsService.updateUsername(optionalUser.get().getId(), (String) requestBody.get("username")));
        return usernameUpdaterReturn;
    }





}
