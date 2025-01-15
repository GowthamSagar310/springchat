package com.gowthamsagar.springchat.controller;

import com.gowthamsagar.springchat.entity.ChatUser;
import com.gowthamsagar.springchat.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor // no need to write constructor. lombok handles
public class UserRegistrationController {

    private final PasswordEncoder passwordEncoder;
    private final ChatUserRepository chatUserRepository;

    @GetMapping("/register")
    public String getRegistrationPage() {
        return "sign-up";
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@ModelAttribute ChatUser chatUser) {

        ModelAndView modelAndView = new ModelAndView("sign-up");
        try {
            String hashedPassword = passwordEncoder.encode(chatUser.getPassword());
            chatUser.setPassword(hashedPassword);
            chatUser.setRole("ROLE_USER");
            ChatUser savedChatUser = chatUserRepository.save(chatUser);
            if (savedChatUser.getId() != null) {
                modelAndView.addObject("message", "User registered successfully, redirecting to login page.");
                modelAndView.addObject("success", true);
            } else {
                modelAndView.addObject("message", "User registration failed.");
                modelAndView.addObject("success", false);
            }
            return modelAndView;
        } catch (DataIntegrityViolationException e) {
            String errorMessage = " User registration failed.";
            if (e.getMessage().contains("users_username_key")) {
                errorMessage += " Username is already taken.";
            } else if (e.getMessage().contains("users_email_key")) {
                errorMessage += " Email is already registered.";
            }
            modelAndView.addObject("message", errorMessage);
            modelAndView.addObject("success", false);
            return modelAndView;
        } catch (Exception e) {
            modelAndView.addObject("message", "User registration failed.");
            // TODO: log the exception trace.
            modelAndView.addObject("success", false);
            return modelAndView;
        }

    }



}
