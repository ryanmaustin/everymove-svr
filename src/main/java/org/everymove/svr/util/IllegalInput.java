package org.everymove.svr.util;

public class IllegalInput extends RuntimeException
{

    public IllegalInput()
    {
        super();
    }
    
    public IllegalInput(String message)
    {
        super(message);
    }
}
