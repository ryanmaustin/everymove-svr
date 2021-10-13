package org.everymove.svr.main.repositories;

import org.everymove.svr.main.structs.Game;

public interface GameRepository 
{
    public Game getGame(String gameId);
    public Game saveGame(Game game);
}
