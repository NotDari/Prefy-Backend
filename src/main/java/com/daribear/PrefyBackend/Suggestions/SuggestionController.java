package com.daribear.PrefyBackend.Suggestions;

import com.daribear.PrefyBackend.Errors.CustomError;
import com.daribear.PrefyBackend.Security.ApplicationUserRole;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controller for handling user's suggestions, both the user submission and the admin retrieval.
 */
@RestController
@RequestMapping(path = "prefy/v1/Suggestions")
@AllArgsConstructor
public class SuggestionController {
    private SuggestionService suggestionService;


    /**
     * Gets a list of all the suggestions and returns them. Requires the user to be an admin to call this
     * @return list of suggestions
     */
    @GetMapping("/AllSuggestions")
    @PreAuthorize("hasRole('ROLE_Admin')")
    public List<Suggestion> getAllSuggestions(){
        return suggestionService.getAllSuggestions();
    }

    /**
     * Submit a new suggestion to the repository.
     *
     * @param suggestion suggestion to be added
     */
    @PostMapping("/Submit")
    public void createNewSuggestion(@RequestBody Suggestion suggestion ){
        suggestionService.addNewSuggestion(suggestion);
    }





}
