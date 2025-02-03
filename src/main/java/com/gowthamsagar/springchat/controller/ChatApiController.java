package com.gowthamsagar.springchat.controller;

import com.gowthamsagar.springchat.dto.ChatDTO;
import com.gowthamsagar.springchat.security.ChatUserDetails;
import com.gowthamsagar.springchat.security.CustomOAuth2User;
import com.gowthamsagar.springchat.service.ChatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatApiController {

    private final ChatsService chatsService;


    @GetMapping("/inbox")
    public ResponseEntity<List<ChatDTO>> getInbox() {
        UUID currentUserId = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            currentUserId = getUserIdFromAuthentication(authentication);
        } else {
            return ResponseEntity.status(401).build();
        }

        // currentUserId should not be null, at this point
        List<ChatDTO> inboxItems = chatsService.getUserInbox(currentUserId);
        return ResponseEntity.ok(inboxItems);
    }

    private UUID getUserIdFromAuthentication(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        UUID userId = null;
        if (principal instanceof CustomOAuth2User) {
            userId = ((CustomOAuth2User) principal).getUserId();
        } else if (principal instanceof ChatUserDetails) {
            userId = ((ChatUserDetails) principal).getUserId();
        }
        return userId;
    }


}
