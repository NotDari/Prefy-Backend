package com.daribear.PrefyBackend.Admin.Script;


import com.daribear.PrefyBackend.Posts.Post;
import lombok.Data;

@Data
public class IncomeAdminPostClass extends Post {
    String username;
    String postUid;
}
