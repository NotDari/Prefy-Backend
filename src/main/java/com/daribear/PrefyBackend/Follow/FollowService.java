package com.daribear.PrefyBackend.Follow;

import com.daribear.PrefyBackend.Activity.ActivityService;
import com.daribear.PrefyBackend.Users.UserService;
import com.daribear.PrefyBackend.Utils.CurrentTime;
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
            follow.setFollowerId(userId);
            follow.setUserId(followerId);
            follow.setFollowDate((double) CurrentTime.getCurrentTime());
            userService.increaseFollowing(userId, true);
            userService.increaseFollowers(followerId, true);
            activityService.alteredFollowing(followerId, userId, true);
            followRepo.save(follow);
        }
    }

    public void unfollow(Long userId, Long followerId){
        Optional<Follow> followOpt = followRepo.findIfExists(userId, followerId);
        if (followOpt.isPresent()){
            activityService.alteredFollowing(followerId, userId, false);
            userService.increaseFollowing(userId, false);
            userService.increaseFollowers(followerId, false);
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
