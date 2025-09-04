package com.daribear.PrefyBackend.Suggestions;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The service for receiving handling the retrieval and submission of user's suggestions.
 */
@Service
@AllArgsConstructor
public class SuggestionService {
    private SuggestionRepository suggestionRepo;


    /**
     * Retrieves all the suggestions from the database
     *
     * @return List of all the suggestions in the database
     */
    public List<Suggestion> getAllSuggestions(){
        return suggestionRepo.findAll();
    }

    /**
     * Saves a new suggestion to the database.
     * @param suggestion suggestion to be saved
     */
    public void addNewSuggestion(Suggestion suggestion){
        suggestionRepo.save(suggestion);
    }

}
