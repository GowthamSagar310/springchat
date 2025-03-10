package com.gowthamsagar.springchat.controller;


import com.gowthamsagar.springchat.dto.MessageDTO;
import com.gowthamsagar.springchat.entity.Message;
import com.gowthamsagar.springchat.entity.key.MessageKey;
import com.gowthamsagar.springchat.security.ChatUserDetails;
import com.gowthamsagar.springchat.security.CustomOAuth2User;
import com.gowthamsagar.springchat.service.MessageService;
import com.gowthamsagar.springchat.service.ParticipantOfChatService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Controller
public class ChatController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private ParticipantOfChatService participantOfChatService;


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

        // notify all participants of the chat
        List<UUID> participants = participantOfChatService.getParticipantsOfChat(messageDTO.getChatId(), 0, 10);
        for (UUID participantUserId : participants) {
            if (!participantUserId.equals(senderId)) { // Don't send notification to sender
                JSONObject notificationPayload = new JSONObject();
                notificationPayload.put("chatId", messageDTO.getChatId().toString());
                notificationPayload.put("messagePreview", messageDTO.getMessage().substring(0, Math.min(messageDTO.getMessage().length(), 50)) + "..."); // Short preview
                notificationPayload.put("senderUsername", senderEmail);
                String inboxTopic = "/topic/inbox/" + participantUserId;
                simpMessagingTemplate.convertAndSend(inboxTopic, notificationPayload.toString());
                System.out.println("Sent inbox notification to topic: " + inboxTopic + ", payload: " + notificationPayload);
            }
        }

        return responseJson.toString();

    }

}
