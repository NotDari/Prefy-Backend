package com.daribear.PrefyBackend.IncomeClasses;

import lombok.Data;

@Data
public class IncomePostListBySearch {
    private Integer pageNumber;
    private Integer limit;
    private String search;
    private String orderBy;
    private Integer minVoteCount;
    private Integer maxVoteCount;
    private Boolean popular;
    private Boolean featured;
    private Boolean deleted;

}
