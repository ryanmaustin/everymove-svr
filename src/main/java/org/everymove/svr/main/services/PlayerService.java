package org.everymove.svr.main.services;

import java.time.LocalDateTime;
import java.util.Set;

import org.everymove.svr.main.structs.Credentials;
import org.everymove.svr.main.structs.Player;
import org.everymove.svr.main.structs.PlayerProfile;
import org.everymove.svr.util.PlayerProfileUpdate;

/**
 * A Service that performs Player management tasks.
 */
public interface PlayerService 
{
    /**
     * Retrieve a Player's Profile based on the given username. This method
     * should not return null. Instead a UserNotFoundException should be 
     * thrown to ensure that this method is thread safe.
     */
    public PlayerProfile getProfile(String username);
    
    /**
     * Get a Player based on the give username. Be aware that retrieving
     * a Player means you are also retrieving their credentials. Treat
     * this method with respect and use it only when you need to.
     */
    public Player getPlayer(String username);

    /**
     * Add a Player (sign them up)
     */
    public void addPlayer(Player player);

    /**
     * Remove a Player (remove them from the Data Base)
     */
    public boolean removePlayer(Player player);

    /**
     * Bans a Player until a certain Date and Time
     */
    public void banPlayer(Player player, LocalDateTime until);
    
    /**
     * Notify the Player Service that a Player has logged in
     */
    public void login(Player player);

   /**
     * Notify the Player Service that a Player has logged out
     */
    public void logout(Player player);

    /**
     * Returns the Current Users logged into the Player Service
     */
    public Set<Player> getCurrentUsers();

    /**
     * Update the Player's Profile
     */
    public Player updatePlayerProfile(Player player, PlayerProfileUpdate... updates);

    /**
     * Update the Credentials. The Credentials should carry the actual password before
     * encoding.
     */
    public Player updatePlayerCredentials(Player player, Credentials credentials);
}
