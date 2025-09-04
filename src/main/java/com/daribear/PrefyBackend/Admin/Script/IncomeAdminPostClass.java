package com.daribear.PrefyBackend.Admin.Script;


import com.daribear.PrefyBackend.Posts.Post;
import lombok.Data;

/**
 * The data for the sending of a post via the request body, with an added username, and postUid.
 */
@Data
public class IncomeAdminPostClass extends Post {
    String username;
    String postUid;
}
