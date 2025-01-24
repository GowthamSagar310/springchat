package com.gowthamsagar.springchat.security;

import com.gowthamsagar.springchat.entity.ChatUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

// this class helps us to get the UUID userId during authentication.

@Getter
public class ChatUserDetails extends User {

    private final UUID userId;

    public ChatUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, UUID userId) {
        super(username, password, authorities);
        this.userId = userId;
    }

    public ChatUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, UUID userId) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userId = userId;
    }

    public ChatUserDetails(ChatUser chatUser, List<GrantedAuthority> authorities) {
        this(chatUser.getUsername(), chatUser.getPassword(), authorities, chatUser.getId());
    }

}
