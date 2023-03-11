package com.daribear.PrefyBackend.IncomeClasses;

import lombok.Data;

@Data
public class IncomePostListById {
    private Long id;
    private Integer pageNumber;
    private Integer limit;
}
