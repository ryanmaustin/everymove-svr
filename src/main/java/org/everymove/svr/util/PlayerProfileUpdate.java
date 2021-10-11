package org.everymove.svr.util;

/**
 * Utility object used to update a Player's Profile. This is locked down
 * to ensure that functionality cannot be added, keeping its use simple and safe.
 */
public class PlayerProfileUpdate 
{
    private String key;
    private Object value;

    private PlayerProfileUpdate()
    {
        // hide
    }

    public String key() 
    {
        return this.key;
    }

    public Object value() 
    {
        return this.value;
    }

    /**
     * The Key should match a field (in lower camel case) belonging to the Player's
     * Profile object. The Value should be of the same type as the field belonging
     * to that Profile. An IllegalArgumentException will be thrown if there is no
     * match to the Player Profile object.
     */
    public static PlayerProfileUpdate of(String key, Object value)
    {
        PlayerProfileUpdate update = new PlayerProfileUpdate();
            update.key = key;
            update.value = value;
        return update;
    }
    
}
