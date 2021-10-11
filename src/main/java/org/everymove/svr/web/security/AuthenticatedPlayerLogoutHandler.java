package org.everymove.svr.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.everymove.svr.main.services.PlayerService;
import org.everymove.svr.main.structs.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedPlayerLogoutHandler implements LogoutSuccessHandler 
{

    private PlayerService playerService;

    @Autowired
    public AuthenticatedPlayerLogoutHandler(PlayerService playerService) 
    {
        this.playerService = playerService;
    }

    @Override
    public void onLogoutSuccess(
        HttpServletRequest request, 
        HttpServletResponse response, 
        Authentication authentication
    ) throws IOException, ServletException 
    {
        if (authentication == null) 
        {
            return;
        }
        UsernamePasswordAuthenticationToken authToken = 
            (UsernamePasswordAuthenticationToken) authentication;

        // Logout of User Service
        Player player = (Player) authToken.getPrincipal();
        playerService.logout(player);
    }
}