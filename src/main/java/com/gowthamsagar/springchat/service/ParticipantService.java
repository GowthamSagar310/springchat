package com.gowthamsagar.springchat.service;

import com.gowthamsagar.springchat.entity.Participant;
import com.gowthamsagar.springchat.repository.ChatUserRepository;
import com.gowthamsagar.springchat.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private ParticipantRepository participantRepository;

    public List<UUID> getUserIdsForChat(UUID chatId, int page, int size) {
        Slice<Participant> participants = participantRepository.findParticipantsById_ChatId(chatId, CassandraPageRequest.of(page, size));
        return participants.getContent().stream()
                .map( participant -> participant.getId().getChatId())
                .collect(Collectors.toList());
    }

    public List<UUID> getChatIdsForUser(UUID userId, int page, int size) {
        Slice<Participant> participants = participantRepository.findParticipantsById_UserId(userId, CassandraPageRequest.of(page, size));
        return participants.getContent().stream()
                .map( participant -> participant.getId().getChatId())
                .collect(Collectors.toList());
    }


}
