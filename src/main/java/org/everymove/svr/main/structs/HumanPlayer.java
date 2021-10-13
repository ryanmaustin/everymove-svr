package org.everymove.svr.main.structs;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class HumanPlayer implements Player
{

    private PlayerProfile profile;

    public HumanPlayer()
    {
        // empty
    }

    public void setProfile(PlayerProfile profile)
    {
        this.profile = profile;
    }

    public PlayerProfile getProfile()
    {
        return this.profile;
    }

    public HumanPlayer profile(PlayerProfile profile)
    {
        setProfile(profile);
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() 
    {
        return null;
    }

    @Override
    public String getPassword() 
    {
        return this.profile.getCredentials().getPassword();
    }

    @Override
    public String getUsername() 
    {
        return this.profile.getCredentials().getUsername();
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
        return true;
    }

    @Override
    public boolean isEnabled() 
    {
        return false;
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
    public String getName() 
    {
        return this.profile.getPlayerId();
    }

    
}
