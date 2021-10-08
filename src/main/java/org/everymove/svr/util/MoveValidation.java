package org.everymove.svr.util;

import org.everymove.svr.main.structs.Move;

public class MoveValidation 
{

    private MoveValidation() 
    {
        // hiding
    }

    public static void validate(Move move)
    {
        AssertThat.notNull(move.getEndingPosition(), "Move cannot be null");
        AssertThat.notNull(move.getStartingPosition(), "Starting Position cannot be null");
        AssertThat.notNull(move.getEndingPosition(), "Starting Position cannot be null");
    }

}
