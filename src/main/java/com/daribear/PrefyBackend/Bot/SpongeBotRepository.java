package com.daribear.PrefyBackend.Bot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The repository handling the details of the Sponebot data entity.
 */
@Repository
public interface SpongeBotRepository extends JpaRepository<SpongebotParameters, Long> {

    /**
     * Retrieve the spongebot paremeters if they exist
     * @return Optional with the spongebot parameters
     */
    @Query("SELECT S FROM SpongebotParameters S")
    Optional<SpongebotParameters> getSpongebotParameters();

}
