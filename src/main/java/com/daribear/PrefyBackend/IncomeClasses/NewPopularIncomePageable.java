package com.daribear.PrefyBackend.IncomeClasses;

import lombok.Data;

import java.util.ArrayList;

/**
 * NewPopularIncomePageable data entity that contains the user id, the list of post ids to ignore and the limit of posts
 * to retreieve,
 */
@Data
public class NewPopularIncomePageable {
    private Integer limit;
    private Long userId;
    private ArrayList<Long> ignoreList;
}
