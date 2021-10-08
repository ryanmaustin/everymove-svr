package org.everymove.svr.main;

import org.everymove.svr.main.structs.Move;

public interface MovesRepository 
{
    public Move update(Move move);
    public Move get(Move move);
}
