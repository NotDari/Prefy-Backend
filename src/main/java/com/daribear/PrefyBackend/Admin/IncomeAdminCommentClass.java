package com.daribear.PrefyBackend.Admin;

import com.daribear.PrefyBackend.Comments.Comment;
import lombok.Data;


@Data
public class IncomeAdminCommentClass extends Comment {
    private String username;
}
