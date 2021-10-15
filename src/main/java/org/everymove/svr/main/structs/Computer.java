package org.everymove.svr.main.structs;

import java.util.Collection;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;

public class Computer implements Player
{
    private String id;
    private final int rating;

    public Computer(int rating)
    {
        this.id = UUID.randomUUID().toString();
        this.rating = rating;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() 
    {
        return null;
    }

    @Override
    public String getPassword() 
    {
        return null;
    }

    @Override
    public String getUsername() 
    {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() 
    {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() 
    {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() 
    {
        return false;
    }

    @Override
    public boolean isEnabled() 
    {
        return false;
    }

    @Override
    public String getName() 
    {
        return this.id;
    }

    @Override
    public PlayerProfile getProfile() 
    {
        return null;
    }

    @Override
    public void setAccountNonExpired(boolean b) 
    {
    }

    @Override
    public void setAccountNonLocked(boolean b) 
    {
    }

    @Override
    public void setCredentialsNonExpired(boolean b) 
    {
    }

    @Override
    public void setEnabled(boolean b) 
    {
    }

    @Override
    public int hashCode()
    {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Computer)) return false;
        return ((Computer) o).id.equals(this.id);
    }

    public int getRating()
    {
        return this.rating;
    }
}
