package com.gowthamsagar.springchat.repository;

import com.gowthamsagar.springchat.entity.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatUserRepository extends JpaRepository<ChatUser, UUID> {
    Optional<ChatUser> findByUsername(String username);
    Optional<ChatUser> findByEmail(String email);
}