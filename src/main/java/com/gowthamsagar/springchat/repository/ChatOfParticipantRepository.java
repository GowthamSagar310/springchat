package com.gowthamsagar.springchat.repository;

import com.gowthamsagar.springchat.entity.ChatOfParticipant;
import com.gowthamsagar.springchat.entity.ParticipantOfChat;
import com.gowthamsagar.springchat.entity.key.ChatOfParticipantKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatOfParticipantRepository extends CassandraRepository<ChatOfParticipant, ChatOfParticipantKey> {

    List<ChatOfParticipant> findById_UserId(UUID userId, Pageable pageable);

}
