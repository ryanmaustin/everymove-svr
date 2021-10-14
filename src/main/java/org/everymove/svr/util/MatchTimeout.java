package org.everymove.svr.util;

public class MatchTimeout extends RuntimeException 
{

    public MatchTimeout(String playerId)
    {
        super("Unable to find a Match for " + playerId);
    }
    
}
