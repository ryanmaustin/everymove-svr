package org.everymove.svr.main.structs;

import java.util.Objects;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Credentials 
{
    /**
     * User Name must be at least 6 characters in length, but less than 20.
     */
    @NotNull
    @Size(min = 6, max = 20)
    private String username;

    @Size(min = 8, max = 60)
    private transient String password;

    /**
     * Email Address must not be null AND must be a valid email address
     */
    @NotNull
    @Email
    private String email;

    public Credentials() 
    {
        // empty
    }

    public String getUsername() 
    {
        return this.username;
    }

    public void setUsername(String username) 
    {
        this.username = username;
    }

    public String getPassword() 
    {
        return this.password;
    }

    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getEmail() 
    {
        return this.email;
    }

    public void setEmail(String email) 
    {
        this.email = email;
    }

    public Credentials withUsername(String username) 
    {
        this.username = username;
        return this;
    }

    public Credentials withPassword(String password) 
    {
        this.password = password;
        return this;
    }

    public Credentials withEmail(String email) 
    {
        this.email = email;
        return this;
    }

    @Override
    public boolean equals(Object o) 
    {
        if (o == this)
            return true;
        if (!(o instanceof Credentials)) 
        {
            return false;
        }
        Credentials credentials = (Credentials) o;
        return Objects.equals(username, credentials.username) && Objects.equals(email, credentials.email);
    }

    @Override
    public int hashCode() 
    {
        return Objects.hash(username, email);
    }

}
