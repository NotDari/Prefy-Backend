package com.daribear.PrefyBackend.CurrentVote;

import com.daribear.PrefyBackend.IncomeClasses.IncomePostListByCategory;
import com.daribear.PrefyBackend.IncomeClasses.IncomeVoteListRetreiver;
import com.daribear.PrefyBackend.Posts.Post;
import com.daribear.PrefyBackend.Posts.PostService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * Controller which handles the endpoints for queries related ti current votes.
 */
@RestController
@RequestMapping(path = "prefy/v1/Votes")
@NoArgsConstructor
public class CurrentVoteController {
    private CurrentVoteService currentVoteService;


    //Dependency injection
    @Autowired
    public CurrentVoteController(CurrentVoteService currentVoteService){
        this.currentVoteService = currentVoteService;
    }

    /**
     * Return a list of votes for a user for a post
     * @param incomeVoteListRetreiver Class containing a user and a list of post ids to get the vote
     * @return the list of votes
     */
    @GetMapping("/VoteList")
    public ArrayList<CurrentVote> getPostListByCategory(IncomeVoteListRetreiver incomeVoteListRetreiver){
        return currentVoteService.getCurrentVoteList(incomeVoteListRetreiver);
    }
}
