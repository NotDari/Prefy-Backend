package com.daribear.PrefyBackend.Activity;


import com.daribear.PrefyBackend.Activity.UserActivity.CommentsActivity;
import com.daribear.PrefyBackend.Activity.UserActivity.UserActivityResetter;
import com.daribear.PrefyBackend.Activity.UserActivity.VotesActivity;
import com.daribear.PrefyBackend.Activity.UserActivity.UserActivity;
import com.daribear.PrefyBackend.Authentication.Authentication;
import com.daribear.PrefyBackend.Authentication.AuthenticationService;
import com.daribear.PrefyBackend.Errors.ErrorStorage;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "prefy/v1/Activity")
@AllArgsConstructor
public class ActivityController {

    private ActivityService activityService;
    private AuthenticationService authService;

    @GetMapping("/CommentsActivity")
    public List<CommentsActivity> getCommentsActivity(Principal principal, @RequestParam Integer pageNumber){
        if (principal == null){
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.InternalError);
        }
        Optional<Authentication> optionalUser = authService.getUserByEmail(principal.getName());
        if (optionalUser.isEmpty()){
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.InternalError);
        }
        return activityService.getCommentsActivity(pageNumber, optionalUser.get().getId());
    }

    @GetMapping("/GeneralActivity")
    public UserActivity getUsersActivity(Principal principal){
        if (principal == null){
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.InternalError);
        }
        Optional<Authentication> optionalUser = authService.getUserByEmail(principal.getName());
        if (optionalUser.isEmpty()){
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.InternalError);
        }
        return activityService.getUserActivity(optionalUser.get().getId());
    }
    @GetMapping("/VotesActivity")
    public List<VotesActivity> getVotesActivity(Principal principal, @RequestParam Integer pageNumber){
        if (principal == null){
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.InternalError);
        }
        Optional<Authentication> optionalUser = authService.getUserByEmail(principal.getName());
        if (optionalUser.isEmpty()){
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.InternalError);
        }
        return activityService.getVotesActivity(pageNumber, optionalUser.get().getId());
    }

    @PostMapping("/SetUserActivity")
    public void setUserActivity(@RequestBody UserActivityResetter userActivityResetter){
        activityService.setUserActivity(userActivityResetter.getId(), userActivityResetter.getNewCommentsCount(),userActivityResetter.getNewVotesCount());
    }



}
