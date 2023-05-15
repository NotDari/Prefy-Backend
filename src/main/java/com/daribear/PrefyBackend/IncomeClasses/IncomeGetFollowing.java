package com.daribear.PrefyBackend.IncomeClasses;

import lombok.Data;

import java.util.ArrayList;


@Data
public class IncomeGetFollowing {
    private Long userId;
    private ArrayList<Long> followerList;
}
