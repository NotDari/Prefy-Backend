package com.daribear.PrefyBackend.IncomeClasses;


import lombok.Data;

@Data
public class IncomePostFollowing {
    private Long userId;
    private Long followId;
    private Boolean follow;

}
