package org.everymove.svr.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService extends DaoAuthenticationProvider 
{
    protected static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    public AuthenticationService(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) 
    {
        this.setUserDetailsService(userDetailsService);
        this.setPasswordEncoder(passwordEncoder);
    }

    @Override
    public boolean supports(Class<?> authentication) 
    {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
