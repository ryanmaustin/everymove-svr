package org.everymove.svr.web.security;

import javax.servlet.http.HttpServletRequest;

import org.everymove.svr.main.structs.Guest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@Configuration
public class GuestConfig 
{
    @Bean
    public AnonymousAuthenticationFilter guestAuthenticationFilter()
    {
        return new GuestAuthenticationFilter("supersecure123");
    }

    public static class GuestAuthenticationFilter extends AnonymousAuthenticationFilter 
    {
        private final String key;

        public GuestAuthenticationFilter(String key) 
        {
            super(key);
            this.key = key;
        }

        @Override
        protected Authentication createAuthentication(HttpServletRequest req) 
        {
            return new AnonymousAuthenticationToken(key, new Guest(), AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        }
    }
}
