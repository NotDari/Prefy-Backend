package com.daribear.PrefyBackend.IncomeClasses;

import lombok.Data;

/**
 * IncomePostListBySearch data entity that contains all of the parameters for an admin searching for posts.
 */
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
