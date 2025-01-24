package com.gowthamsagar.springchat.controller;

import com.gowthamsagar.springchat.security.ChatUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {

        String username = null;
        UUID userId = null;
        if (authentication != null && authentication.isAuthenticated()) {
         Object principal = authentication.getPrincipal();
         if (principal instanceof OAuth2User) {
             username = ((OAuth2User) principal).getAttribute("login").toString().toLowerCase();
             // how do you get the UUID userId for GitHub ?
         } else if (principal instanceof UserDetails) {
             ChatUserDetails userDetails = (ChatUserDetails) principal;
             username = userDetails.getUsername();
             userId = userDetails.getUserId();
         }
        } else {
            return "index";
        }
        if (username != null) {
            model.addAttribute("username", username );
            model.addAttribute("userId", userId);
            return "home";
        }
        return "index";
    }

}
