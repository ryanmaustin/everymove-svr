package org.everymove.svr.main.versus;

import org.everymove.svr.main.structs.Game;
import org.everymove.svr.main.structs.Player;

public interface MatchmakingQueue 
{
    public void addPlayer(Player player);
    public Game getMatch(Player player);
}
