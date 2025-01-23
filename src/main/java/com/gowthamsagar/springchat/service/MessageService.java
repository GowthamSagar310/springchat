package com.gowthamsagar.springchat.service;

import com.gowthamsagar.springchat.dto.ChatMessage;
import com.gowthamsagar.springchat.entity.Message;
import com.gowthamsagar.springchat.entity.MessageKey;
import com.gowthamsagar.springchat.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private MessageRepository messageRepository;

    // gets messages using repository
    public List<Message> getMessagesForChat(UUID chatId, int page, int size) {

        // how to use pageable ?
        // pageable is made of page_index + batch_size
        // first 20 messages -> 0, 20
        // next 20 messages -> 1, 20

        Slice<Message> messages = messageRepository.findMessagesById_ChatId(chatId, CassandraPageRequest.of(page, size));
        return messages.getContent();

    }

    // save a message using repository
    public void saveMessage(Message message) {
        messageRepository.save(message);
    }

    // create and save message record in DB
    public void createAndSaveMessage(ChatMessage chatMessage) {
        Message message = new Message();

        MessageKey messageKey = new MessageKey();
        messageKey.setMessageId(UUID.randomUUID());
        messageKey.setChatId(chatMessage.getChatId());
        messageKey.setCreatedAt(Instant.now());

        message.setId(messageKey);
        message.setContent(chatMessage.getContent());
        saveMessage(message);
    }

}
