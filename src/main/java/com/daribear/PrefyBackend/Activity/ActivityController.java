package com.daribear.PrefyBackend.Activity;


import com.daribear.PrefyBackend.Activity.UserActivity.Comments.CommentsActivity;
import com.daribear.PrefyBackend.Activity.UserActivity.Follows.FollowActivity;
import com.daribear.PrefyBackend.Activity.UserActivity.UserActivityResetter;
import com.daribear.PrefyBackend.Activity.UserActivity.Votes.VotesActivity;
import com.daribear.PrefyBackend.Activity.UserActivity.UserActivity;
import com.daribear.PrefyBackend.Authentication.Authentication;
import com.daribear.PrefyBackend.Authentication.AuthenticationService;
import com.daribear.PrefyBackend.Errors.ErrorStorage;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * Controller for all of the activity retrieval.
 * Used of comments, user, vote, follow  as well as the total count activity services.
 *
 */
@RestController
@RequestMapping(path = "prefy/v1/Activity")
@AllArgsConstructor
public class ActivityController {

    private ActivityService activityService;
    private AuthenticationService authService;

    /**
     * Retrieve the comments activity of the user.
     * Used to paginate through the comments activity.
     *
     * @param principal the authentication principal for getting the user
     * @param pageNumber the page number of comments activity to get.
     * @return list of commentsActivities.
     */
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

    /**
     * Retrieve the general activity of the user.
     * Retrieves the total counts of activities, so only 1 object per user and thus no pagination
     *
     * @param principal the authentication principal for getting the user
     * @return list of commentsActivities.
     */
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

    /**
     * Retrieve the votes activity of the user.
     * Used to paginate through the votes activity.
     *
     * @param principal the authentication principal for getting the user
     * @param pageNumber the page number of votes activity to get.
     * @return list of VotesActivities.
     */
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
    /**
     * Retrieve the followers activity of the user.
     * Used to paginate through the followers activity.
     *
     * @param principal the authentication principal for getting the user
     * @param pageNumber the page number of votes followers to get.
     * @return list of FollowActivities
     */
    @GetMapping("/FollowersActivity")
    public List<FollowActivity> getFollowersActivity(Principal principal, @RequestParam Integer pageNumber){
        if (principal == null){
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.InternalError);
        }
        Optional<Authentication> optionalUser = authService.getUserByEmail(principal.getName());
        if (optionalUser.isEmpty()){
            throw ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.InternalError);
        }
        return activityService.getFollowersActivity(pageNumber, optionalUser.get().getId());
    }


    /**
     * Alters the totalActivity counts(stored in UserActivity) to the values set in userActivityResetter.
     *
     * @param userActivityResetter the values to alter the user's current UserActivity's values to
     */
    @PostMapping("/SetUserActivity")
    public void setUserActivity(@RequestBody UserActivityResetter userActivityResetter){
        activityService.setUserActivity(userActivityResetter.getId(), userActivityResetter.getNewCommentsCount(),userActivityResetter.getNewVotesCount(), userActivityResetter.getNewFollowsCount());
    }



}
