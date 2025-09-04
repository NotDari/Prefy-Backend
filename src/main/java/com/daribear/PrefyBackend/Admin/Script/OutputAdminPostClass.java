package com.daribear.PrefyBackend.Admin.Script;

import lombok.Data;

/**
 * The data for the output response of an admin post. Sends the uid and id of the post.
 */
@Data
public class OutputAdminPostClass {
    private String uid;
    private Long postId;
}
