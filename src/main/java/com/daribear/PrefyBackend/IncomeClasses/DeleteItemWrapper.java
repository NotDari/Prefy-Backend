package com.daribear.PrefyBackend.IncomeClasses;


import lombok.Data;


/**
 * Default Item wrapper data entity that has item Id and user id.
 */
@Data
public class DeleteItemWrapper {
    private Long itemId;
    private Long userId;
}
