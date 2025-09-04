package com.daribear.PrefyBackend.Admin.Script;

import com.daribear.PrefyBackend.CurrentVote.CurrentVote;
import lombok.Data;

/**
 * The data for the sending of a currentvote via the request body, with an added username.
 */
@Data
public class IncomeAdminVoteClass extends CurrentVote {
    private String username;
}
