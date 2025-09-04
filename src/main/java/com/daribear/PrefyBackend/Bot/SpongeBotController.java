package com.daribear.PrefyBackend.Bot;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 *
 */
@RestController
@RequestMapping(path = "prefy/v1/Spongebot")
public class SpongeBotController  {
    private SpongebotService sbotService;
    private Spongebot spongebot;

    public SpongeBotController() {
    }

    @Autowired
    public SpongeBotController(SpongebotService sbotService, Spongebot spongebot){
        this.sbotService = sbotService;
        this.spongebot = spongebot;
    }


    @PostMapping("/SwitchBotOn/{on}")
    public void checkSpongebot(@PathVariable Boolean on){
        Optional<SpongebotParameters> optional = sbotService.getSpongeBotParameters();
        if (optional.isPresent()){
            SpongebotParameters params = optional.get();
            if (params.getBotOn() != on) {
                params.setBotOn(on);
                sbotService.updateSpongeBotParameters(params);
            }
        }
        if (on) {
            spongebot.run();
        }
    }

    @GetMapping("/ResetSpongebot")
    public void resetSpongebot(){
        SpongebotParameters spongebotParameters = new SpongebotParameters("Spongebot", false, false,false,  0L, 0L, 0L, 0, 0);
        sbotService.updateSpongeBotParameters(spongebotParameters);

    }

}
