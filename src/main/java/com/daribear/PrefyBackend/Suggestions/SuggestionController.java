package com.daribear.PrefyBackend.Suggestions;

import com.daribear.PrefyBackend.Errors.CustomError;
import com.daribear.PrefyBackend.Security.ApplicationUserRole;
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

@RestController
@RequestMapping(path = "prefy/v1/Suggestions")
public class SuggestionController {
    private SuggestionService suggestionService;

    @Autowired
    public SuggestionController(SuggestionService postService){
        this.suggestionService = postService;
    }

    @GetMapping("/AllSuggestions")
    public List<Suggestion> getAllSuggestions(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Set<String> roles = authentication.getAuthorities().stream()
                .map(r -> r.getAuthority()).collect(Collectors.toSet());
        //throw new CustomError(HttpServletResponse.SC_UNAUTHORIZED, "Test", 1);
        return suggestionService.getAllSuggestions();
    }

    @PostMapping("/Submit")
    public void createNewSuggestion(@RequestBody Suggestion suggestion ){
        suggestionService.addNewSuggestion(suggestion);
    }





}
