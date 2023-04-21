package com.daribear.PrefyBackend.IncomeClasses;

import lombok.Data;

import java.util.ArrayList;

@Data
public class NewPopularIncomePageable {
    private Integer limit;
    private Long userId;
    private ArrayList<Long> ignoreList;
}
