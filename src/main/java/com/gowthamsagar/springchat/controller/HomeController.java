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
             OAuth2User oAuth2User = (OAuth2User) principal;
             // handle userId
             // todo: what permissions do i need to ask, for username details ?
             // store github username. not the mail.
         } else if (principal instanceof UserDetails) {
             UserDetails userDetails = (UserDetails) principal;
             userId = userDetails.getUsername();
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
