package org.everymove.svr.main.services;

import org.everymove.svr.main.repositories.PlayerRepository;
import org.everymove.svr.main.structs.Player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PlayerDetailsService implements UserDetailsService 
{
    protected static final Logger log = LoggerFactory.getLogger(PlayerDetailsService.class);

	private PlayerRepository repo;

	@Autowired
    public PlayerDetailsService(PlayerRepository repo) 
    {
		this.repo = repo;
    }

	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
    {
		log.info("Loading details for Player [{}]", username);
        try 
        {
			Player player = repo.get(username);
                player.setAccountNonExpired(true);
				player.setAccountNonLocked(true);
				player.setCredentialsNonExpired(true);
				player.setEnabled(true);
			return player;

        } 
        catch (Exception e) 
        {
			log.warn("Player [{}] not retrieved. Reason: {}", username, e.getLocalizedMessage());
			throw new UsernameNotFoundException(e.getLocalizedMessage());
		}
	}
}
