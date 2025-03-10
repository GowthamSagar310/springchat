package com.gowthamsagar.springchat.repository;

import com.gowthamsagar.springchat.entity.ParticipantOfChat;
import com.gowthamsagar.springchat.entity.key.ParticipantOfChatKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ParticipantOfChatRepository extends CassandraRepository<ParticipantOfChat, ParticipantOfChatKey> {

    List<ParticipantOfChat> findById_ChatId(UUID chatId, Pageable pageable);

}
