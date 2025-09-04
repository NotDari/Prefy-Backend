package com.daribear.PrefyBackend.Follow;

import com.daribear.PrefyBackend.Activity.ActivityService;
import com.daribear.PrefyBackend.Users.UserService;
import com.daribear.PrefyBackend.Utils.CurrentTime;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

/**
 * The service class for handling all follow actions, whether making one user follow/unfollow another, or checking
 * whether a list of user's is followed by the provided user.
 */
@Service
@AllArgsConstructor
public class FollowService {
    private FollowRepository followRepo;
    private ActivityService activityService;

    private UserService userService;

    /**
     * Makes one user follow another, creating the follow activity to notify the user, and increasing users follower count.
     *
     * @param userId User who is following
     * @param followingId User who is being followed
     */
    public void follow(Long userId, Long followingId){
        Optional<Follow> followOpt = followRepo.findIfExists(userId, followingId);
        if (followOpt.isEmpty()){
            Follow follow = new Follow();
            follow.setFollowerId(userId);
            follow.setUserId(followingId);
            follow.setFollowDate((double) CurrentTime.getCurrentTime());
            userService.alterFollowingCount(userId, true);
            userService.alterFollowersCount(followingId, true);
            activityService.alteredFollowing(followingId, userId, true);
            followRepo.save(follow);
        }
    }

    /**
     * Makes one user unfollow another, altering following/follower count.
     *
     * @param userId person who is unfollowing
     * @param followingId user who is being unfollowed
     */
    public void unfollow(Long userId, Long followingId){
        Optional<Follow> followOpt = followRepo.findIfExists(userId, followingId);
        if (followOpt.isPresent()){
            activityService.alteredFollowing(followingId, userId, false);
            userService.alterFollowingCount(userId, false);
            userService.alterFollowersCount(followingId, false);
            followRepo.delete(followOpt.get());
        }
    }

    /**
     * Checks for a list of users, if the current user is following them.
     * @param followingList list of users to check if being followed by selected user
     * @param userId id of user to select to check if following
     * @return (HashMap<Long, Boolean>) - userId mapped to whether the selected user is following them
     */
    public HashMap<Long, Boolean> getFollowingList(ArrayList<Long> followingList, Long userId){
        HashMap<Long, Boolean> following = new HashMap<>();
        for (Long id : followingList){
            Optional<Follow> followOpt = followRepo.findIfExists(userId, id);
            if (followOpt.isPresent()){
                following.put(id, true);
            } else {
                following.put(id, false);
            }
        }

        return following;
    }


}
