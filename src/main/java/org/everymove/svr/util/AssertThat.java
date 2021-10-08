package org.everymove.svr.util;

public class AssertThat 
{

    private AssertThat()
    {
        // hiding
    }

    public static void isTrue(Boolean bool, String message)
    {
        if (bool == null || !bool) throw new IllegalInput(message);
    }

    public static void notNull(Object o, String message)
    {
        if (o == null) throw new IllegalInput(message);
    }

}
