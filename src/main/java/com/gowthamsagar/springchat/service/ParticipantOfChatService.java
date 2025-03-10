package com.gowthamsagar.springchat.service;

import com.gowthamsagar.springchat.entity.ParticipantOfChat;
import com.gowthamsagar.springchat.repository.ParticipantOfChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ParticipantOfChatService {

    @Autowired
    private ParticipantOfChatRepository participantOfChatRepository;

    public List<UUID> getParticipantsOfChat(UUID chatId, int size, int page) {
        List<ParticipantOfChat> participants = participantOfChatRepository.findById_ChatId(chatId, PageRequest.of(page, size));
        return participants
                .stream()
                .map( participant -> participant.getId().getUserId())
                .collect(Collectors.toList());
    }


}
