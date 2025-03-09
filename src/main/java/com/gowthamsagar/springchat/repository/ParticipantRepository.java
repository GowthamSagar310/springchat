package com.gowthamsagar.springchat.repository;

import com.gowthamsagar.springchat.entity.Participant;
import com.gowthamsagar.springchat.entity.key.ParticipantKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ParticipantRepository extends CassandraRepository<Participant, ParticipantKey> {

    Slice<Participant> findParticipantsById_ChatId(UUID chatId, Pageable pageable);
    Slice<Participant> findParticipantsById_UserId(UUID userId, Pageable pageable);

    Slice<Participant> findById(ParticipantKey participantKey, Pageable pageable);


}
