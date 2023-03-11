package com.daribear.PrefyBackend.IncomeClasses;

import lombok.Data;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Data
public class IncomeVoteListRetreiver {
    private ArrayList<Long> postIdList;
    private Long userId;
}
