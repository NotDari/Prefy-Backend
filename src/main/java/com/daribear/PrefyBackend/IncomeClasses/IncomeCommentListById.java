package com.daribear.PrefyBackend.IncomeClasses;

import lombok.Data;

import java.util.ArrayList;

@Data
public class IncomeCommentListById {
    private Long id;
    private Integer pageNumber;
    private Integer limit;
}
