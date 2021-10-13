package org.everymove.svr.main.structs;

import java.util.HashSet;
import java.util.UUID;

public class Guest extends HumanPlayer 
{

    public Guest()
    {
        final String id = UUID.randomUUID().toString();
        // Create a fake player for now
        PlayerProfile profile = new PlayerProfile();
        profile.setFirstName("");
        profile.setLastName("");
        profile.setGameIds(new HashSet<>());
        profile.setPlayerId(id);

        Credentials credentials = new Credentials();
        credentials.setEmail("");
        credentials.setUsername(id);
        credentials.setPassword("");
        profile.setCredentials(credentials);

        this.setProfile(profile);
    }

}
