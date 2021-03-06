package org.everymove.svr.web;

import org.everymove.svr.main.repositories.PlayerRepository;
import org.everymove.svr.web.security.WebSocketHandshakeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer 
{   
    protected static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

    @Autowired
    private PlayerRepository profileRepository;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) 
    {
        config.enableSimpleBroker("game");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/players");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) 
    {
        registry.addEndpoint("/wsgame").setAllowedOrigins("http://localhost:4200").setHandshakeHandler(new WebSocketHandshakeHandler(profileRepository));
        registry.addEndpoint("/wsgame").setAllowedOrigins("http://localhost:4200").setHandshakeHandler(new WebSocketHandshakeHandler(profileRepository)).withSockJS();
    }

    @EventListener
    public void onSocketConnected(SessionConnectEvent event)
    {
        StompHeaderAccessor stompHeaderAccessor = wrapEvent(event);
        final String userDn = getUserDn(stompHeaderAccessor);
        final String sessionId = stompHeaderAccessor.getSessionId();
    
        logger.info(
            "Web Socket Connected -> Session Id: {}, User: {}",
            sessionId,
            userDn
        );
    }
    
    @EventListener
    public void onSocketDisconnected(SessionDisconnectEvent event)
    {
        StompHeaderAccessor stompHeaderAccessor = wrapEvent(event);
        final String userDn = getUserDn(stompHeaderAccessor);
        final String sessionId = stompHeaderAccessor.getSessionId();

        logger.info(
            "Web Socket Disconnected -> Instance Id: {}, User: {}",
            sessionId,
            userDn
        );
    }

    protected String getUserDn(StompHeaderAccessor accessor)
    {
      return accessor.getUser().getName();
    }

    public StompHeaderAccessor wrapEvent(AbstractSubProtocolEvent event)
    {
        return StompHeaderAccessor.wrap(event.getMessage());
    }
}