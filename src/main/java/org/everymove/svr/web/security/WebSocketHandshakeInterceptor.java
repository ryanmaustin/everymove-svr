package org.everymove.svr.web.security;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Component
@SuppressWarnings({"unchecked", "rawtypes"})
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor 
{
    protected static Logger log = LoggerFactory.getLogger(WebSocketHandshakeInterceptor.class);

    @Override
    public boolean beforeHandshake(
        ServerHttpRequest request, 
        ServerHttpResponse response, 
        WebSocketHandler wsHandler,
        Map attributes
    ) throws Exception 
    {
        if (request instanceof ServletServerHttpRequest) 
        {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession();
            attributes.put("sessionId", session.getId());
            log.debug("Session {} created", session.getId());
            return true;
        }
        return false;
    }

    @Override
    public void afterHandshake
    (
        ServerHttpRequest request, 
        ServerHttpResponse response, 
        WebSocketHandler wsHandler,
        Exception ex
    ) 
    {        
        // do nothing
    }
}