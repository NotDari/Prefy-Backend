package com.daribear.PrefyBackend.IncomeClasses;

import lombok.Data;

import java.util.ArrayList;

/**
 * IncomeUserIdList data entity that contains a list of user ids.
 */
@Data
public class IncomeUserIdList {
    private ArrayList<Long> idList;
}
