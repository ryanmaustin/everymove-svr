package org.everymove.svr.web.security;

import java.security.Principal;
import java.util.Map;

import org.everymove.svr.main.repositories.PlayerRepository;
import org.everymove.svr.main.structs.Guest;
import org.everymove.svr.main.structs.Player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
public class WebSocketHandshakeHandler extends DefaultHandshakeHandler
{

    private PlayerRepository profileRepository;
    
    @Autowired
    public WebSocketHandshakeHandler(PlayerRepository profileRepository)
    {
        this.profileRepository = profileRepository;
    }

    @Override
    protected Principal determineUser(
        ServerHttpRequest request,
        WebSocketHandler wsHandler,
        Map<String, Object> attributes
    ) 
    {
        if (request.getPrincipal() != null)
        {
            return this.profileRepository.get(request.getPrincipal().getName());
        }
        Player guest = new Guest();
        this.profileRepository.add(guest);
        return guest;
    }

}
