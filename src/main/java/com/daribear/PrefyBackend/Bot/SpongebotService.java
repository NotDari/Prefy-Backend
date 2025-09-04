package com.daribear.PrefyBackend.Bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * The service which handles the service level actions for the Spongebot data entity.
 */
@Service
@Component
public class SpongebotService {
    private SpongeBotRepository botRepo;


    public SpongebotService() {
    }
    //Dependancy injection
    @Autowired
    public SpongebotService(SpongeBotRepository botRepo){
        this.botRepo = botRepo;
    }

    /**
     * Get the spongebot parameters
     * @return get the list of spongebot parameters if it exists
     */
    public Optional<SpongebotParameters> getSpongeBotParameters(){
        return botRepo.getSpongebotParameters();
    }

    /**
     * Update the spongebot parameters
     * @param spongebotParameters parameters to update with
     */
    public void updateSpongeBotParameters(SpongebotParameters spongebotParameters){
        botRepo.save(spongebotParameters);
    }
}
