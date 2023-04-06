package com.daribear.PrefyBackend.IncomeClasses;

import lombok.Data;

@Data
public class PopularIncomePageable{
    private Integer limit;
    private Long userId;
    private Double lastPopularDate;
}
