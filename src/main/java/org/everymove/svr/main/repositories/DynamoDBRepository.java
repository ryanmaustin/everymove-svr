package org.everymove.svr.main.repositories;


import org.everymove.svr.main.structs.Game;
import org.everymove.svr.main.structs.Guest;
import org.everymove.svr.main.structs.Move;
import org.everymove.svr.main.structs.Player;
import org.springframework.stereotype.Repository;

@Repository
public class DynamoDBRepository implements MovesRepository, PlayerProfileRepository, GameRepository
{
    // TODO: Remove later once real implementation is in place
    private Player player;


    @Override
    public Move update(Move move) 
    {
        return null;
    }

    @Override
    public Move get(Move move) 
    {
        return null;
    }

    @Override
    public Player get(String username) 
    {
        if (player != null) return player;

        player = new Guest();

        return player;
    }

    @Override
    public Game getGame(String gameId) 
    {
        return null;
    }

    @Override
    public Game saveGame(Game game) 
    {
        return null;
    }

    @Override
    public void add(Player player) 
    {
        this.player = player;    
    }
    
}
