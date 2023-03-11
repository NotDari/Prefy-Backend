package com.daribear.PrefyBackend.Authentication.Bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Component
public class SpongebotService {
    private SpongeBotRepository botRepo;


    public SpongebotService() {
    }

    @Autowired
    public SpongebotService(SpongeBotRepository botRepo){
        System.out.println("Sdad Repo" + botRepo);
        this.botRepo = botRepo;
    }


    public Optional<SpongebotParameters> getSpongeBotParameters(){
        return botRepo.getSpongebotParameters();
    }

    public void updateSpongeBotParameters(SpongebotParameters spongebotParameters){
        botRepo.save(spongebotParameters);
    }
}
