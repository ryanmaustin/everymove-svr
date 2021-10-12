package org.everymove.svr.main.services;

import java.time.LocalDateTime;
import java.util.Set;

import org.everymove.svr.main.structs.Credentials;
import org.everymove.svr.main.structs.Player;
import org.everymove.svr.main.structs.PlayerProfile;
import org.everymove.svr.util.PlayerProfileUpdate;
import org.springframework.stereotype.Service;

@Service
public class MasterPlayerService implements PlayerService
{

    @Override
    public PlayerProfile getProfile(String username) 
    {
        return null;
    }

    @Override
    public Player getPlayer(String username) {
        return null;
    }

    @Override
    public void addPlayer(Player player) {
        
    }

    @Override
    public boolean removePlayer(Player player) {
        return false;
    }

    @Override
    public void banPlayer(Player player, LocalDateTime until) 
    {
        
    }

    @Override
    public void login(Player player) 
    {
        
    }

    @Override
    public void logout(Player player) 
    {
        
    }

    @Override
    public Set<Player> getCurrentUsers() 
    {
        return null;
    }

    @Override
    public Player updatePlayerProfile(Player player, PlayerProfileUpdate... updates) 
    {
        return null;
    }

    @Override
    public Player updatePlayerCredentials(Player player, Credentials credentials) 
    {
        return null;
    }
    
}
