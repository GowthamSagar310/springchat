package com.gowthamsagar.springchat.controller;

import com.gowthamsagar.springchat.dto.ChatDTO;
import com.gowthamsagar.springchat.security.ChatUserDetails;
import com.gowthamsagar.springchat.service.ChatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.UUID;

@Controller
public class HomeController {

    @Autowired
    private ChatsService chatsService;


    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {

        String userEmail = null;
        UUID userId = null;
        if (authentication != null && authentication.isAuthenticated()) {
         Object principal = authentication.getPrincipal();
         if (principal instanceof OAuth2User) {
             userEmail = ((OAuth2User) principal).getAttribute("login").toString().toLowerCase();
             // how do you get the UUID userId for GitHub ?
         } else if (principal instanceof UserDetails) {
             ChatUserDetails userDetails = (ChatUserDetails) principal;
             userEmail = userDetails.getUsername();
             userId = userDetails.getUserId();
         }
        } else {
            return "index";
        }

        if (userId != null) { // Check if userId is obtained
            List<ChatDTO> inboxChats = chatsService.getUserInbox(userId); // Fetch inbox chats
            model.addAttribute("chats", inboxChats); // Add chats to the model
            model.addAttribute("userEmail", userEmail);
            model.addAttribute("userId", userId);
            return "home";
        }
        return "index"; // ideally this should not be executed.
    }

}
