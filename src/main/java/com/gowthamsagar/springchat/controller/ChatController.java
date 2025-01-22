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
        String username = null;
        if (authentication != null && authentication.getPrincipal() != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof OAuth2User) {
                OAuth2User oAuth2User = (OAuth2User) principal;
                // handle userId
                // todo: what permissions do i need to ask, for username details ?
                // store github username. not the mail.
                username = oAuth2User.getAttribute("name");
            } else if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                username = userDetails.getUsername();
            }
        } else {
            System.out.println("user not logged in. could not send message");
        }

        JSONObject responseJson = new JSONObject();
        responseJson.put("username", username);
        responseJson.put("message", new JSONObject(message).get("message"));
        return responseJson.toString();

    }

}
