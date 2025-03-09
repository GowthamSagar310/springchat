package com.gowthamsagar.springchat.service;

import com.gowthamsagar.springchat.dto.MessageDTO;
import com.gowthamsagar.springchat.entity.Message;
import com.gowthamsagar.springchat.entity.key.MessageKey;
import com.gowthamsagar.springchat.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    @Autowired
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
    public void createAndSaveMessage(Message chatMessage, UUID senderId) {
        Message message = new Message();

        MessageKey messageKey = new MessageKey();
        messageKey.setMessageId(UUID.randomUUID());

        // even this can be tampered. need to be careful.
        // todo: validate chatId
        messageKey.setChatId(chatMessage.getId().getChatId());
        messageKey.setCreatedAt(Instant.now());

        message.setId(messageKey);
        message.setContent(chatMessage.getContent());

        // should not blindly trust the chatMessage.getSenderId() as it can be tampered.
        // senderId should always be from the server side.
        // message.setSenderId(chatMessage.getSenderId());
        message.setSenderId(senderId);

        saveMessage(message);
    }

}
