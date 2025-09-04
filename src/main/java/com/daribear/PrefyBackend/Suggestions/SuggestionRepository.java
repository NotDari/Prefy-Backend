package com.daribear.PrefyBackend.Suggestions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the suggestions.
 */
@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {



}
