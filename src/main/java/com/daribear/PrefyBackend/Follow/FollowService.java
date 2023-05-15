package com.daribear.PrefyBackend.Follow;

import com.daribear.PrefyBackend.Activity.ActivityService;
import com.daribear.PrefyBackend.Users.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FollowService {
    private FollowRepository followRepo;
    private ActivityService activityService;

    private UserService userService;


    public void follow(Long userId, Long followerId){
        Optional<Follow> followOpt = followRepo.findIfExists(userId, followerId);
        if (followOpt.isEmpty()){
            Follow follow = new Follow();
            follow.setFollowerId(followerId);
            follow.setUserId(userId);
            follow.setFollowDate((double) System.currentTimeMillis());
            userService.increaseFollowing(followerId, true);
            userService.increaseFollowers(userId, true);
            activityService.alteredFollowing(userId, followerId, true);
            followRepo.save(follow);
        }
    }

    public void unfollow(Long userId, Long followerId){
        Optional<Follow> followOpt = followRepo.findIfExists(userId, followerId);
        if (followOpt.isPresent()){
            activityService.alteredFollowing(userId, followerId, false);
            userService.increaseFollowing(followerId, false);
            userService.increaseFollowers(userId, false);
            followRepo.delete(followOpt.get());
        }
    }

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
