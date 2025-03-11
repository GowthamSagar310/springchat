package com.gowthamsagar.springchat;

import com.gowthamsagar.springchat.entity.Chat;
import com.gowthamsagar.springchat.entity.ChatOfParticipant;
import com.gowthamsagar.springchat.entity.ChatUser;
import com.gowthamsagar.springchat.entity.ParticipantOfChat;
import com.gowthamsagar.springchat.entity.key.ChatOfParticipantKey;
import com.gowthamsagar.springchat.entity.key.ParticipantOfChatKey;
import com.gowthamsagar.springchat.repository.ChatOfParticipantRepository;
import com.gowthamsagar.springchat.repository.ChatRepository;
import com.gowthamsagar.springchat.repository.ChatUserRepository;
import com.gowthamsagar.springchat.repository.ParticipantOfChatRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.UUID;

@SpringBootApplication
@EnableCassandraRepositories(basePackages = "com.gowthamsagar.springchat.repository")
public class SpringChatApplication {

    private final ParticipantOfChatRepository participantOfChatRepository;
    private final ChatOfParticipantRepository chatOfParticipantRepository;

    public SpringChatApplication(ParticipantOfChatRepository participantOfChatRepository, ChatOfParticipantRepository chatOfParticipantRepository, ChatOfParticipantRepository chatOfParticipantRepository1) {
        this.participantOfChatRepository = participantOfChatRepository;
        this.chatOfParticipantRepository = chatOfParticipantRepository1;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringChatApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(
            PasswordEncoder passwordEncoder,
            ChatUserRepository userRepository,
            ChatRepository chatRepository
    ) {
//         return args -> {
//            ChatUser admin = ChatUser.builder()
//                    .username("admin")
//                    .email("admin@test.com")
//                    .password(passwordEncoder.encode("dummy"))
//                    .role("USER")
//                    .build();
//
//            ChatUser test1 = ChatUser.builder()
//                    .username("test1")
//                    .email("test1@test.com")
//                    .password(passwordEncoder.encode("dummy"))
//                    .role("USER")
//                    .build();
//
//            ChatUser test2 = ChatUser.builder()
//                    .username("test2")
//                    .email("test2@test.com")
//                    .password(passwordEncoder.encode("dummy"))
//                    .role("USER")
//                    .build();
//
//            userRepository.save(admin);
//            userRepository.save(test1);
//            userRepository.save(test2);

            return args -> {
                // --- Create Users ---

                ChatUser userA = createUser(userRepository, passwordEncoder, "usera", "usera@test.com", "dummy", "USER"); // User A
                ChatUser userB = createUser(userRepository, passwordEncoder, "userb", "userb@test.com", "dummy", "USER"); // User B
                ChatUser userC = createUser(userRepository, passwordEncoder, "userc", "userc@test.com", "dummy", "USER"); // User C

                // --- Create Chats ---
                UUID chat1Id = UUID.fromString("a1b2c3d4-e5f6-7890-1234-567890abcdef"); // Example UUID - replace with your own
                Chat chat1 = createChat(chatRepository, chat1Id, "one-to-one");

                UUID chat2Id = UUID.fromString("b1c2d3e4-f5a6-8901-2345-678901abcdef"); // Example UUID - replace with your own
                Chat chat2 = createChat(chatRepository, chat2Id, "one-to-one");

                // group chat
                UUID chat3Id = UUID.fromString("c1d2e3f4-a5b6-9012-3456-789012abcdef"); // Example UUID - replace with your own
                Chat chat3 = createChat(chatRepository, chat3Id, "group");


                // --- Create Participants ---
                createParticipant(chat1Id, userA.getId());
                createParticipant(chat1Id, userB.getId());

                createParticipant(chat2Id, userA.getId());
                createParticipant(chat2Id, userC.getId());

                createParticipant(chat3Id, userA.getId());
                createParticipant(chat3Id, userB.getId());
                createParticipant(chat3Id, userC.getId());

            };
    }

    private ChatUser createUser(ChatUserRepository userRepository, PasswordEncoder passwordEncoder, String username, String email, String password, String role) {
        ChatUser user = ChatUser.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(role)
                .build();
        return userRepository.save(user);
    }

    private Chat createChat(ChatRepository chatRepository, UUID chatId, String type) {
        Chat chat = new Chat();
        chat.setId(chatId);
        chat.setType(type);
        chat.setCreatedAt(Instant.now());
        return chatRepository.save(chat);
    }

    private void createParticipant(UUID chatId, UUID userId) {
        // what if only one of the statement gets executed ? this will leave the database in inconsistent state.
        participantOfChatRepository.save(new ParticipantOfChat(new ParticipantOfChatKey(chatId, userId, Instant.now())));
        chatOfParticipantRepository.save(new ChatOfParticipant(new ChatOfParticipantKey(userId, chatId, Instant.now())));

    }


}
