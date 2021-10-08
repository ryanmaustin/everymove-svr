package org.everymove.svr.main;

import org.everymove.svr.main.structs.Move;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Moves 
{

    protected final MovesRepository repository;

    @Autowired
    public Moves(MovesRepository repository)
    {
        this.repository = repository;
    }

    public Move update(Move move)
    {
        return this.repository.update(move);
    }

    public Move get(Move move)
    {
        return this.repository.get(move);
    }
    
}
