package com.gowthamsagar.springchat.security.securityservice;

import com.gowthamsagar.springchat.entity.ChatUser;
import com.gowthamsagar.springchat.repository.ChatUserRepository;
import com.gowthamsagar.springchat.security.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final ChatUserRepository chatUserRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");
        if (chatUserRepository.findByEmail(email).isPresent()) {
            // this email already exists.
            // that means, the user has already signed up using email and password.
            // so prompt the user to login using email and password.
            // this ensures there is only one account per email.
            throw new OAuth2AuthenticationException(new OAuth2Error("email_exists"), "Email address is already registered.");
        }
        return oAuth2User;
    }


}
