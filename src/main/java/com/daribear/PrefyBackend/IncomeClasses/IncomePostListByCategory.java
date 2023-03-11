package com.daribear.PrefyBackend.IncomeClasses;

import lombok.Data;

@Data
public class IncomePostListByCategory {
    private String category;
    private Integer pageNumber;
    private Integer limit;
}
