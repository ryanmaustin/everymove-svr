package org.everymove.svr.main.structs;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.apache.commons.lang3.text.WordUtils;
import org.hibernate.validator.constraints.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Player Profile data structure
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerProfile 
{
    protected static final Logger log = LoggerFactory.getLogger(PlayerProfile.class);
    
    @NotBlank
    @Pattern(regexp = "^[\\p{L} .'-]+$")
    private String firstName;

    @NotBlank
    @Pattern(regexp = "^[\\p{L} .'-]+$")
    private String lastName;

    @URL
    private String profilePhotoUrl;

    private Set<String> gameIds;

    private Credentials credentials;

    public PlayerProfile() 
    {
        // empty
    }

    public String getFirstName() 
    {
        return this.firstName;
    }

    public void setFirstName(String firstName) 
    {
        this.firstName = firstName;
    }

    public String getLastName() 
    {
        return this.lastName;
    }

    public void setLastName(String lastName) 
    {
        this.lastName = lastName;
    }

    public String getProfilePhotoUrl() 
    {
        return this.profilePhotoUrl;
    }

    public String getFullName() 
    {
        return WordUtils.capitalize(this.firstName) + " "
            + WordUtils.capitalize(this.lastName);
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) 
    {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public Set<String> getGameIds() 
    {
        if (this.gameIds == null) this.gameIds = new HashSet<>();
        return this.gameIds;
    }

    public void setGameIds(Set<String> visitIds) 
    {
        this.gameIds = visitIds;
    }

    public void setCredentials(Credentials credentials)
    {
        this.credentials = credentials;
    }

    public Credentials getCredentials()
    {
        return this.credentials;
    }

    public PlayerProfile credentials(Credentials credentials)
    {
        this.setCredentials(credentials);
        return this;
    }

    public PlayerProfile profilePhotoUrl(String profilePhotoUrl) 
    {
        this.profilePhotoUrl = profilePhotoUrl;
        return this;
    }

    public PlayerProfile firstName(String firstName) 
    {
        this.firstName = firstName;
        return this;
    }

    public PlayerProfile lastName(String lastName) 
    {
        this.lastName = lastName;
        return this;
    }

}
