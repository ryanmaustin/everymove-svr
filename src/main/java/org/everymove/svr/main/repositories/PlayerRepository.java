package org.everymove.svr.main.repositories;

import org.everymove.svr.main.structs.Player;

public interface PlayerRepository 
{

	public Player get(String playerId);

	public void add(Player player);
    
}
