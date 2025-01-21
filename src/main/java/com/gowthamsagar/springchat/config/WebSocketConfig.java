package com.gowthamsagar.springchat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic/");

        // client needs to send the message to /app/sendMessage
        // spring maps it to sendMessage controller

        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        // this is for the initial handshake endpoint for client to establish connection

        registry.addEndpoint("/chat");

        // ws://localhost:8080/chat
        // ws://<domain>/chat

        // what is withSockJS() doing ?
    }


}
