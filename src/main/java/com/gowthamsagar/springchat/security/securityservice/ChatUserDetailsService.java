package com.gowthamsagar.springchat.security.securityservice;

import com.gowthamsagar.springchat.entity.ChatUser;
import com.gowthamsagar.springchat.repository.ChatUserRepository;
import com.gowthamsagar.springchat.security.ChatUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatUserDetailsService implements UserDetailsService {

    @Autowired
    private ChatUserRepository chatUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ChatUser chatUser = chatUserRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + username)
        );
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(chatUser.getRole()));

        return new ChatUserDetails(chatUser.getUsername(), chatUser.getPassword(), grantedAuthorities, chatUser.getId());
        // return new ChatUserDetails(chatUser, grantedAuthorities)

    }

}