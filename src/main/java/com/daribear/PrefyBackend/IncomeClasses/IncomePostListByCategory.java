package com.daribear.PrefyBackend.IncomeClasses;

import lombok.Data;


/**
 * IncomePostListByCategory data entity that contains a category and pagination details.
 */
@Data
public class IncomePostListByCategory {
    private String category;
    private Integer pageNumber;
    private Integer limit;
}
