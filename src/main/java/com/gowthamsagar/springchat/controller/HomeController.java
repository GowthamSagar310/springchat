package com.gowthamsagar.springchat.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(Authentication authentication) {
        String userId = null;
        if (authentication != null && authentication.isAuthenticated()) {
         Object principal = authentication.getPrincipal();
         if (principal instanceof OAuth2User) {
             userId = ((OAuth2User) principal).getAttribute("login").toString().toLowerCase();
         } else if (principal instanceof UserDetails) {
             userId = ((UserDetails) principal).getUsername();
         }
        } else {
            return "index";
        }
        if (userId != null) {
            // get inbox
            // get messages
            return "home";
        }
        return "index";
    }

}
