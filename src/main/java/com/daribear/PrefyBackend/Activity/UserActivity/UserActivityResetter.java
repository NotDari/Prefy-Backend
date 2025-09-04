package com.daribear.PrefyBackend.Activity.UserActivity;


import lombok.Data;

/**
 * An entity used to store values which will update and "reset" a USerActivity entity.
 */
@Data
public class UserActivityResetter {
    private Long id;
    private Long newCommentsCount;
    private Long newVotesCount;
    private Long newFollowsCount;
}
