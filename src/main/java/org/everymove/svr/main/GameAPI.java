package org.everymove.svr.main;

import java.security.Principal;

import org.everymove.svr.main.structs.GameRequest;
import org.everymove.svr.main.structs.MoveRequest;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Controller
public class GameAPI 
{
    protected SimpMessagingTemplate msgr;
    protected GameEngine engine;

    @Autowired
    public GameAPI(SimpMessagingTemplate msgr, GameEngine engine)
    {
        this.msgr = msgr;
        this.engine = engine;
    }

    @MessageMapping("/game/request")
    public void requestNewGame(
        SimpMessageHeaderAccessor sha, 
        @Payload GameRequest gameRequest,
        Principal challenger,
        @Header("simpSessionId") String sessionId
    )
    {
        this.engine.requestNewGame(challenger, gameRequest);
    }

    @MessageMapping("/game/respond")
    public void acceptNewGame(
        SimpMessageHeaderAccessor sha, 
        @Payload GameRequest gameRequest,
        Principal opponent,
        @Header("simpSessionId") String sessionId
    )
    {
        this.engine.acceptChallenge(opponent, gameRequest);
    }

    @MessageMapping("/game/move")
    public void makeMove(
        SimpMessageHeaderAccessor sha, 
        @Payload MoveRequest moveRequest, 
        Principal player,
        @Header("simpSessionId") String sessionId
    )
    {
        this.engine.makeMove(moveRequest);
    }

}
