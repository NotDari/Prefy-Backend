package com.daribear.PrefyBackend.Follow;


import com.daribear.PrefyBackend.IncomeClasses.IncomeGetFollowing;
import com.daribear.PrefyBackend.IncomeClasses.IncomePostFollowing;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping(path = "prefy/v1/Follows")
@AllArgsConstructor
public class FollowController {
    private FollowService followService;

    @GetMapping("/GetFollowing")
    public HashMap<Long, Boolean> getFollowList(IncomeGetFollowing incomeGetFollowing){
        return followService.getFollowingList(incomeGetFollowing.getFollowerList(), incomeGetFollowing.getUserId());
    }

    @PostMapping("/Follow")
    public void submitFollow(@RequestBody IncomePostFollowing incomePostFollowing){
        if (incomePostFollowing.getFollow()){
            followService.follow(incomePostFollowing.getUserId(), incomePostFollowing.getFollowId());
        }else {
            followService.unfollow(incomePostFollowing.getUserId(), incomePostFollowing.getFollowId());
        }

    }


}
