package org.everymove.svr.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sleeper 
{
    private static final Logger logger = LoggerFactory.getLogger(Sleeper.class);

    public static void sleep(long ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
            logger.error("Thread was interupted because: {}", e.getLocalizedMessage(), e);
        }
    }

}
