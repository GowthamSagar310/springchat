package com.gowthamsagar.springchat.service;

import com.gowthamsagar.springchat.dto.ChatDTO;
import com.gowthamsagar.springchat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatsService {

    private final ChatRepository chatRepository;

    public List<ChatDTO> getUserInbox(UUID currentUserId) {



    }
}
