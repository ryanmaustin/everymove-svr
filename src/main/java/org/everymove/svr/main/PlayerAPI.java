package org.everymove.svr.main;

import javax.servlet.http.HttpServletRequest;

import org.everymove.svr.main.services.PlayerService;
import org.everymove.svr.main.structs.Player;
import org.everymove.svr.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PlayerAPI 
{
    protected PlayerService playerService;

    @Autowired
    public PlayerAPI(PlayerService playerService)
    {
        this.playerService = playerService;
    }

    /**
     * This endpoint returns a Player's Profile when they login.
     */
    @GetMapping(path = "/player/login")
    public Response userLogin(Player player, HttpServletRequest request) 
    {
        playerService.login(player);
        return Response.ok(player.getProfile());
    }

}
