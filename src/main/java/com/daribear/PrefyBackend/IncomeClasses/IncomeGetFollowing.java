package com.daribear.PrefyBackend.IncomeClasses;

import lombok.Data;

import java.util.ArrayList;



/**
 * Incoming Get Following entity that has the user id and the list of follower ids.
 */
@Data
public class IncomeGetFollowing {
    private Long userId;
    private ArrayList<Long> followerList;
}
