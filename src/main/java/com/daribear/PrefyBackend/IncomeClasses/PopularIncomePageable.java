package com.daribear.PrefyBackend.IncomeClasses;

import lombok.Data;

/**
 * PopularIncomePageable data entity that contains a user id, the last date that the user saw and the max number of posts to retrieve.
 */
@Data
public class PopularIncomePageable{
    private Integer limit;
    private Long userId;
    private Double lastPopularDate;
}
