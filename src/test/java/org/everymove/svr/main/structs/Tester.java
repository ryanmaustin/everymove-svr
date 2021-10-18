package org.everymove.svr.main.structs;

import java.util.Collection;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;

public class Tester implements Player
{

    private String id;

    public Tester()
    {
        this.id = UUID.randomUUID().toString();
    }

    public Tester(String nameOrId)
    {
        this.id = nameOrId;
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
    
}
