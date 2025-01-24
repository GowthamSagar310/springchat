package com.gowthamsagar.springchat.repository;

import com.gowthamsagar.springchat.entity.Message;
import com.gowthamsagar.springchat.entity.key.MessageKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageRepository extends CassandraRepository<Message, MessageKey> {

    // get list of messages
    Slice<Message> findMessagesById_ChatId(UUID chatId, Pageable pageable);


}
