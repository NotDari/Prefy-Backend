package com.daribear.PrefyBackend.IncomeClasses;

import lombok.Data;

import java.util.ArrayList;


/**
 * IncomePostIdList data entity that contains a list of post ids.
 */
@Data
public class IncomePostIdList {
    private ArrayList<Long> idList;
}
