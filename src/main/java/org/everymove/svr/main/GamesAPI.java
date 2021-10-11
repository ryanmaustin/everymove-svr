package org.everymove.svr.main;

import org.everymove.svr.main.structs.Game;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Controller
public class GamesAPI 
{
    @Autowired
    protected SimpMessagingTemplate msgr;

    @MessageMapping("/newgame")
    public void newGame(SimpMessageHeaderAccessor sha, @Payload Game game, String username)
    {
        Game newGame = game == null ? new Game() : Game.fromExisting(game);
        msgr.convertAndSendToUser(username, "/games/moves", newGame);
    }

}
