package com.daribear.PrefyBackend.Admin.Script;

import com.daribear.PrefyBackend.CurrentVote.CurrentVote;
import lombok.Data;

@Data
public class IncomeAdminVoteClass extends CurrentVote {
    private String username;
}
