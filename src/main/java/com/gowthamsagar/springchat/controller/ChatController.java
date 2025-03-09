package com.gowthamsagar.springchat.controller;


import com.gowthamsagar.springchat.dto.MessageDTO;
import com.gowthamsagar.springchat.entity.Message;
import com.gowthamsagar.springchat.entity.key.MessageKey;
import com.gowthamsagar.springchat.security.ChatUserDetails;
import com.gowthamsagar.springchat.security.CustomOAuth2User;
import com.gowthamsagar.springchat.service.MessageService;
import com.gowthamsagar.springchat.service.ParticipantService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.UUID;

@Controller
public class ChatController {

    private final MessageService messageService;
    private final ParticipantService participantService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public ChatController(MessageService messageService, ParticipantService participantService) {
        this.messageService = messageService;
        this.participantService = participantService;
    }

    // client sends the message to /app/sendMessage
    @MessageMapping("/sendMessage")
    // @SendTo("/topic/public") // instead of sending it to a public channel, re-route to topic based on chatId
    public String sendMessage(@Payload MessageDTO messageDTO, Authentication authentication) {

        UUID senderId = null; // id of the user who sent the message
        String senderEmail = null; // email of the user who sent the message
        if (authentication != null && authentication.getPrincipal() != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof OAuth2User) {
                CustomOAuth2User customOAuth2User = (CustomOAuth2User) principal;
                senderEmail = customOAuth2User.getAttribute("login").toString().toLowerCase();
                senderId = customOAuth2User.getUserId();
            } else if (principal instanceof UserDetails) {
                ChatUserDetails userDetails = (ChatUserDetails) principal;
                senderEmail = userDetails.getUsername();
                senderId = userDetails.getUserId();
            }
        } else {
            // TODO: log this
            System.out.println("User not logged in. Could not send message");
        }

        if (senderId == null) {
            // TODO: log this
            System.out.println("Could not determine userId. Could not send message");
            return null;
        }

        // for now, Instant.now() is used as the createdAt time.
        // but it can be changed to the time when the message is received by the server.
        // which can be stored in the kafka / any other message queue.

        MessageKey messageKey = new MessageKey(UUID.randomUUID(), messageDTO.getChatId(), Instant.now());

        Message message = new Message();
        message.setId(messageKey);
        message.setSenderId(senderId);
        message.setContent(messageDTO.getMessage());

        messageService.createAndSaveMessage(message, senderId);

        // change this code based on chat type.
        // Participant participant = new Participant(new ParticipantKey(chatMessage.getChatId(), userId));

        // participantService.saveParticipant();

        // todo: 2. add participants in chat if not there
        // todo: 3. add in chat table if not there.
        // todo: 4. add {username, userid, content, type}
        // is it optimized to do all these for every query ?

        JSONObject responseJson = new JSONObject();
        responseJson.put("userId", senderId);
        responseJson.put("userEmail", senderEmail);
        responseJson.put("message", message.getContent());

        String topic = "/topic/chat/" + message.getId().getChatId().toString();
        simpMessagingTemplate.convertAndSend(topic, responseJson.toString());

        return responseJson.toString();

    }

}
