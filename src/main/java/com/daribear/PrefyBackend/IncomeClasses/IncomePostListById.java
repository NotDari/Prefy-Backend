package com.daribear.PrefyBackend.IncomeClasses;

import lombok.Data;


/**
 * IncomePostListById data entity that contains an id and pagination details.
 */
@Data
public class IncomePostListById {
    private Long id;
    private Integer pageNumber;
    private Integer limit;
}
