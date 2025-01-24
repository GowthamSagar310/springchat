package com.gowthamsagar.springchat.controller;


import com.gowthamsagar.springchat.dto.ChatMessage;
import com.gowthamsagar.springchat.service.MessageService;
import com.gowthamsagar.springchat.service.ParticipantService;
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

    private final MessageService messageService;
    private final ParticipantService participantService;

    public ChatController(MessageService messageService, ParticipantService participantService) {
        this.messageService = messageService;
        this.participantService = participantService;
    }

    // client sends the message to /app/sendMessage
    @MessageMapping("/sendMessage")
    @SendTo("/topic/public")
    public String sendMessage(@Payload ChatMessage chatMessage, Authentication authentication) {

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

        messageService.createAndSaveMessage(chatMessage);

        // change this code based on chat type.
        // Participant participant = new Participant(new ParticipantKey(chatMessage.getChatId(), userId));

        // participantService.saveParticipant();

        // todo: 2. add participants in chat if not there
        // todo: 3. add in chat table if not there.
        // todo: 4. add {username, userid, content, type}
        // is it optimized to do all these for every query ?

        JSONObject responseJson = new JSONObject();
        responseJson.put("userId", userId);
        responseJson.put("message", new JSONObject(chatMessage.getContent()).get("message"));
        return responseJson.toString();

    }

}
