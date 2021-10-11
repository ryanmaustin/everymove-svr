package org.everymove.svr.web.security;

import org.everymove.svr.main.structs.Player;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Allows the Authenticated Player to be retrieved as a parameter within
 * a Controller's endpoint.
 */
public class AuthenticatedPlayerResolver implements HandlerMethodArgumentResolver 
{

    @Override
    public boolean supportsParameter(MethodParameter parameter) 
    {
        return parameter.getParameterType().equals(Player.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception 
    {
        UsernamePasswordAuthenticationToken authToken = 
            (UsernamePasswordAuthenticationToken) webRequest.getUserPrincipal();
	 
	    return (Player) authToken.getPrincipal();
    }
}
