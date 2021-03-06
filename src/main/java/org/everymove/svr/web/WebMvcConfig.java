package org.everymove.svr.web;

import java.util.List;

import org.everymove.svr.web.security.AuthenticatedPlayerResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer 
{
    @Override
    public void addArgumentResolvers(
        List<HandlerMethodArgumentResolver> argumentResolvers) 
    {
        argumentResolvers.add(new AuthenticatedPlayerResolver());
    }
}
