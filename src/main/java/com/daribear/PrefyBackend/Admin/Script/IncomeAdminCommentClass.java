package com.daribear.PrefyBackend.Admin.Script;

import com.daribear.PrefyBackend.Comments.Comment;
import lombok.Data;

/**
 * The data for the sending of a comment via the request body, with an added username.
 */
@Data
public class IncomeAdminCommentClass extends Comment {
    private String username;
}
