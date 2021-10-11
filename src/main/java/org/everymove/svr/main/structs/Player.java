package org.everymove.svr.main.structs;

import org.springframework.security.core.userdetails.UserDetails;

public interface Player extends UserDetails
{

    public PlayerProfile getProfile();

    public void setAccountNonExpired(boolean b);

	public void setAccountNonLocked(boolean b);

	public void setCredentialsNonExpired(boolean b);

	public void setEnabled(boolean b);

}
