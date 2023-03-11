package com.daribear.PrefyBackend.Suggestions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuggestionService {
    private SuggestionRepository suggestionRepo;


    @Autowired
    public SuggestionService(SuggestionRepository suggestionRepo){
        this.suggestionRepo = suggestionRepo;
    }

    public List<Suggestion> getAllSuggestions(){
        return suggestionRepo.findAll();
    }

    public void addNewSuggestion(Suggestion post){
        suggestionRepo.save(post);
    }

}
