package com.daribear.PrefyBackend.IncomeClasses;

import lombok.Data;

import java.util.ArrayList;


/**
 * Default Comment List data entity that has the post id and pagination details.
 */
@Data
public class IncomeCommentListById {
    private Long id;
    private Integer pageNumber;
    private Integer limit;
}
