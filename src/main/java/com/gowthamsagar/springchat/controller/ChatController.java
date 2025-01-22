package com.gowthamsagar.springchat.controller;


import org.json.JSONObject;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;


@Controller
public class ChatController {

    // client sends the message to /app/sendMessage
    @MessageMapping("/sendMessage")
    @SendTo("/topic/public")
    public String sendMessage(@Payload String message, Authentication authentication) {
        System.out.println(message);
        String userId = null;
        if (authentication != null && authentication.getPrincipal() != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof OAuth2User) {
                userId = ((OAuth2User) principal).getAttribute("login").toString().toLowerCase();
            } else if (principal instanceof UserDetails) {
                userId = ((UserDetails) principal).getUsername();
            }
        } else {
            System.out.println("user not logged in. could not send message");
        }

        // todo: add messages to DB
        // todo: publish it to only corresponding user

        JSONObject responseJson = new JSONObject();
        responseJson.put("userId", userId);
        responseJson.put("message", new JSONObject(message).get("message"));
        return responseJson.toString();

    }

}
