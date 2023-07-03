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

    @Autowired
    public CurrentVoteService (CurrentVoteRepository currentVoteRepo){
        this.currentVoteRepo = currentVoteRepo;
    }

    public Optional<CurrentVote> getCurrentVote(Long userId, Long postId){
        return currentVoteRepo.findCurrentVote(userId, postId);
    }

    public void saveCurrentVote(CurrentVote currentVote){

        currentVoteRepo.save(currentVote);
    }

    public void updateCurrentVote(Long voteId, String vote){
        currentVoteRepo.updateCurrentVoteVote(voteId, vote);
    }

    public ArrayList<CurrentVote> getCurrentVoteList(IncomeVoteListRetreiver incomeVoteListRetreiver){
        Optional<ArrayList<CurrentVote>> optCurrentVote = currentVoteRepo.findCurrentVoteList(incomeVoteListRetreiver.getUserId(), incomeVoteListRetreiver.getPostIdList());
        /**
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
         */
        if (optCurrentVote.isPresent()){
            return optCurrentVote.get();
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
