package com.daribear.PrefyBackend.IncomeClasses;

import lombok.Data;


/**
 * Default income data entity that has paginatation details.
 */
@Data
public class DefaultIncomePageable {
    private Integer pageNumber;
    private Integer limit;
}
