package com.daribear.PrefyBackend.IncomeClasses;


import lombok.Data;


/**
 * IncomePostFollowing data entity, that contains a user id, the following id and whether or not to follow or unfollow.
 */
@Data
public class IncomePostFollowing {
    private Long userId;
    private Long followId;
    private Boolean follow;

}
