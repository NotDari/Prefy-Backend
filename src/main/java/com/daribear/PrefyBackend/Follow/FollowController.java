package com.daribear.PrefyBackend.Follow;


import com.daribear.PrefyBackend.IncomeClasses.IncomeGetFollowing;
import com.daribear.PrefyBackend.IncomeClasses.IncomePostFollowing;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * Controller for follows, so it calls the service class for handling all follow actions, whether making one user follow/unfollow another, or checking
 *  * whether a list of user's is followed by the provided user.
 *
 */
@RestController
@RequestMapping(path = "prefy/v1/Follows")
@AllArgsConstructor
public class FollowController {
    private FollowService followService;

    /**
     * Calls the service to check who the user is following out of a list of users.
     * @param incomeGetFollowing - contains the id of the user and the list of users to be checked
     * @return (HashMap<Long, Boolean>) - map from userId to whether the user is following them
     */
    @GetMapping("/GetFollowing")
    public HashMap<Long, Boolean> getFollowList(IncomeGetFollowing incomeGetFollowing){
        return followService.getFollowingList(incomeGetFollowing.getFollowerList(), incomeGetFollowing.getUserId());
    }

    /**
     * Makes one user follow/unfollow another.
     *
     * @param incomePostFollowing - contains the userId, the person to follow/unfollow and whether this is an follow or unfollow request
     */
    @PostMapping("/Follow")
    public void submitFollow(@RequestBody IncomePostFollowing incomePostFollowing){
        //Check if the request is to follow/unfollow
        if (incomePostFollowing.getFollow()){
            followService.follow(incomePostFollowing.getUserId(), incomePostFollowing.getFollowId());
        }else {
            followService.unfollow(incomePostFollowing.getUserId(), incomePostFollowing.getFollowId());
        }

    }


}
