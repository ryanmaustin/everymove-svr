package org.everymove.svr.web.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@Configuration
@EnableWebSocketMessageBroker
public class SocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer 
{
    @Override
    protected boolean sameOriginDisabled() 
    {
        return true;
    }

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) 
    {
        messages.anyMessage().permitAll();
    }
}