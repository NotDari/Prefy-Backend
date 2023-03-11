package com.daribear.PrefyBackend.IncomeClasses;

import lombok.Data;

@Data
public class DefaultIncomePageable {
    private Integer pageNumber;
    private Integer limit;
}
