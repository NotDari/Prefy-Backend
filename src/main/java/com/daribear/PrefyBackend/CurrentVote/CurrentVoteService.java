package com.daribear.PrefyBackend.CurrentVote;


import com.daribear.PrefyBackend.IncomeClasses.IncomeVoteListRetreiver;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Service
@Component
public class CurrentVoteService {
    private CurrentVoteRepository currentVoteRepo;

    public CurrentVoteService() {
    }

    //Dependancy injection
    @Autowired
    public CurrentVoteService (CurrentVoteRepository currentVoteRepo){
        this.currentVoteRepo = currentVoteRepo;
    }

    /**
     * Attempts to get a currentVote from a specific user on a specific post.
     *
     * @param userId user id to get vote from
     * @param postId post id to get from from
     * @return Optional<CurrentVote> currentVote if it exists
     */
    public Optional<CurrentVote> getCurrentVote(Long userId, Long postId){
        return currentVoteRepo.findCurrentVote(userId, postId);
    }

    /**
     * Saves a current vote to the repo
     * @param currentVote current vote to be saved
     */
    public void saveCurrentVote(CurrentVote currentVote){

        currentVoteRepo.save(currentVote);
    }

    /**
     * Updates a current vote in the repo.
     * @param voteId vote id to be altered
     * @param vote vote status to update to
     */
    public void updateCurrentVote(Long voteId, String vote){
        currentVoteRepo.updateCurrentVoteVote(voteId, vote);
    }

    /**
     * Gets a list of votes for a user for a list of posts.
     * Posts don't necessarily have to have votes.
     * @param incomeVoteListRetreiver class containing userid and list of post ids
     * @return List of currentVotes
     */
    public ArrayList<CurrentVote> getCurrentVoteList(IncomeVoteListRetreiver incomeVoteListRetreiver){
        Optional<ArrayList<CurrentVote>> optCurrentVote = currentVoteRepo.findCurrentVoteList(incomeVoteListRetreiver.getUserId(), incomeVoteListRetreiver.getPostIdList());
        if (optCurrentVote.isPresent()){
            ArrayList<CurrentVote> retrievedVotes = optCurrentVote.get();
            Integer arraySize = incomeVoteListRetreiver.getPostIdList().size();
            CurrentVote[] currentVoteList = new CurrentVote[arraySize];
            for (int i = 0 ; i < arraySize; i++){
                for (int f = 0; f < retrievedVotes.size(); f ++){
                    if (retrievedVotes.get(f).getPostId().equals(incomeVoteListRetreiver.getPostIdList().get(i))){
                        currentVoteList[i] = retrievedVotes.get(f);
                    }
                }
            }
            return new ArrayList<>(Arrays.asList(currentVoteList));
        }
        return null;

        /**
        ArrayList<CurrentVote> currentVoteList = new ArrayList<>();
        for (int i = 0; i < incomeVoteListRetreiver.getPostIdList().size(); i++){
            Optional<CurrentVote> optCurrentVote = currentVoteRepo.findCurrentVote(incomeVoteListRetreiver.getUserId(), incomeVoteListRetreiver.getPostIdList().get(i));
            if (optCurrentVote.isPresent()){
                currentVoteList.add(optCurrentVote.get());
            } else {
                currentVoteList.add(null);
            }

        }
        return currentVoteList;
         */
    }
}
