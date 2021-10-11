package org.everymove.svr.main.repositories;

import org.everymove.svr.main.structs.Player;

public interface PlayerProfileRepository 
{

	public Player get(String username);
    
}
