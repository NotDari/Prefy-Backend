package com.daribear.PrefyBackend.IncomeClasses;

import lombok.Data;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * IncomeVoteListRetreiver data entity that contains a list of post ids and the user id.
 */
@Data
public class IncomeVoteListRetreiver {
    private ArrayList<Long> postIdList;
    private Long userId;
}
