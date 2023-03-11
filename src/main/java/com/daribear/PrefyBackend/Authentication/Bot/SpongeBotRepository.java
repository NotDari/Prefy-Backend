package com.daribear.PrefyBackend.Authentication.Bot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SpongeBotRepository extends JpaRepository<SpongebotParameters, Long> {

    @Query("SELECT S FROM SpongebotParameters S")
    Optional<SpongebotParameters> getSpongebotParameters();

}
