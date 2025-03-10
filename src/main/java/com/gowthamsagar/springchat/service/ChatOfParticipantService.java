package com.gowthamsagar.springchat.service;

import com.gowthamsagar.springchat.entity.ChatOfParticipant;
import com.gowthamsagar.springchat.repository.ChatOfParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChatOfParticipantService {

    @Autowired
    private ChatOfParticipantRepository chatOfParticipantRepository;

    public List<UUID> getChatsOfParticipant(UUID participantId, int size, int page) {
        List<ChatOfParticipant> chats  = chatOfParticipantRepository.findById_UserId(participantId, PageRequest.of(page, size));
        return chats
                .stream()
                .map( chat -> chat.getId().getChatId())
                .collect(Collectors.toList());
    }

}
