package com.daribear.PrefyBackend.Activity.UserActivity;


import lombok.Data;

@Data
public class UserActivityResetter {
    private Long id;
    private Long newCommentsCount;
    private Long newVotesCount;
}
