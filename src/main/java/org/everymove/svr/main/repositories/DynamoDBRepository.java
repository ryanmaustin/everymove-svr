package org.everymove.svr.main.repositories;

import org.everymove.svr.main.structs.Move;
import org.everymove.svr.main.structs.Player;
import org.springframework.stereotype.Repository;

@Repository
public class DynamoDBRepository implements MovesRepository, PlayerProfileRepository
{

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
        return null;
    }
    
}
