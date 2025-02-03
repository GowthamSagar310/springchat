package com.gowthamsagar.springchat.security.securityservice;

import com.gowthamsagar.springchat.entity.ChatUser;
import com.gowthamsagar.springchat.repository.ChatUserRepository;
import com.gowthamsagar.springchat.security.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final ChatUserRepository chatUserRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        String githubUsername = oAuth2User.getName();
        if (githubUsername != null) {
            Optional<ChatUser> optionalGithubChatUser = chatUserRepository.findByUsername(githubUsername);
            ChatUser chatUser = null;
            if (optionalGithubChatUser.isPresent()) {
                chatUser = optionalGithubChatUser.get();
            } else {
                chatUser = createNewGithubChatUser(oAuth2User);
                chatUserRepository.save(chatUser);
            }
            return new CustomOAuth2User(oAuth2User, chatUser.getId());
        } else {
            throw new OAuth2AuthenticationException("Could not retrieve Github username");
        }
    }

    private ChatUser createNewGithubChatUser(OAuth2User oAuth2User) {
        ChatUser chatUser = new ChatUser();
        chatUser.setUsername(oAuth2User.getAttribute("login"));
        chatUser.setEmail(oAuth2User.getAttribute("email"));
        chatUser.setUsername(oAuth2User.getName());
        chatUser.setRole("USER"); // todo: make it standardized. ENUM
        chatUser.setPassword("oauth2-user-no-password"); // todo: this should not be used to login
        return chatUser;
    }

}
