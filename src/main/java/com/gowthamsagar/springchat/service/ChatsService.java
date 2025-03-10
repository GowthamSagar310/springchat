package com.gowthamsagar.springchat.service;

import com.gowthamsagar.springchat.dto.ChatDTO;
import com.gowthamsagar.springchat.entity.Chat;
import com.gowthamsagar.springchat.entity.ChatUser;
import com.gowthamsagar.springchat.repository.ChatRepository;
import com.gowthamsagar.springchat.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatsService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatUserRepository chatUserRepository;

    @Autowired
    private ChatOfParticipantService chatOfParticipantService;

    @Autowired
    private ParticipantOfChatService participantOfChatService;

    public List<ChatDTO> getUserInbox(UUID currentUserId) {

        // what are the 10 latest chatIds that the current user is a participant of ?
        List<UUID> chatIds = chatOfParticipantService.getChatsOfParticipant(currentUserId, 10, 0);

        // get those chats
        List<Chat> chats = chatRepository.findAllById(chatIds);

        // convert to ChatDTO
        return chats
                .stream()
                .map( chat -> {
                    ChatDTO chatDTO = new ChatDTO();
                    chatDTO.setId(chat.getId());
                    chatDTO.setLastMessageContent(chat.getLastMessageContent());
                    chatDTO.setLastMessageTimestamp(chat.getLastMessageTimestamp());

                    // from the participants, get the participants list
                    List<UUID> participantIds = participantOfChatService.getParticipantsOfChat(chat.getId(), 10, 0);

                    participantIds =  participantIds
                            .stream()
                            .filter( participantId -> !participantId.equals(currentUserId))
                            .collect(Collectors.toList());

                    if (chat.getType().equals("one-to-one") && !participantIds.isEmpty()) {
                        UUID otherParticipantId = participantIds.get(0);
                        ChatUser otherChatUser = chatUserRepository.findById(otherParticipantId).orElse(null);
                        if (otherChatUser != null) {
                            chatDTO.setSenderName(otherChatUser.getEmail()); // Set chat name to other user's username in 1-1 chats
                        } else {
                            chatDTO.setSenderName("Unknown User");
                        }
                    } else if (chat.getType().equals("group")) {
                        chatDTO.setSenderName("Group Chat"); // Or fetch group chat name if you have group names
                    } else {
                        chatDTO.setSenderName("Unknown Chat");
                    }
                    return chatDTO;
                })
                .collect(Collectors.toList());

    }
}
