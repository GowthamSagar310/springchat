package com.gowthamsagar.springchat;

import com.gowthamsagar.springchat.entity.Chat;
import com.gowthamsagar.springchat.entity.ChatUser;
import com.gowthamsagar.springchat.entity.Participant;
import com.gowthamsagar.springchat.entity.key.ParticipantKey;
import com.gowthamsagar.springchat.repository.ChatRepository;
import com.gowthamsagar.springchat.repository.ChatUserRepository;
import com.gowthamsagar.springchat.repository.ParticipantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.UUID;


@SpringBootApplication
public class SpringChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringChatApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(
            PasswordEncoder passwordEncoder,
            ChatUserRepository userRepository,
            ChatRepository chatRepository,
            ParticipantRepository participantRepository
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
                ChatUser admin = createUser(userRepository, passwordEncoder, "admin", "admin@test.com", "dummy", "USER");
                ChatUser test1 = createUser(userRepository, passwordEncoder, "test1", "test1@test.com", "dummy", "USER");
                ChatUser test2 = createUser(userRepository, passwordEncoder, "test2", "test2@test.com", "dummy", "USER");

                ChatUser userA = createUser(userRepository, passwordEncoder, "usera", "usera@test.com", "dummy", "USER"); // User A
                ChatUser userB = createUser(userRepository, passwordEncoder, "userb", "userb@test.com", "dummy", "USER"); // User B
                ChatUser userC = createUser(userRepository, passwordEncoder, "userc", "userc@test.com", "dummy", "USER"); // User C


                // --- Create Chats ---
                UUID chat1Id = UUID.fromString("a1b2c3d4-e5f6-7890-1234-567890abcdef"); // Example UUID - replace with your own
                Chat chat1 = createChat(chatRepository, chat1Id, "one-to-one");

                UUID chat2Id = UUID.fromString("b1c2d3e4-f5a6-8901-2345-678901abcdef"); // Example UUID - replace with your own
                Chat chat2 = createChat(chatRepository, chat2Id, "one-to-one");

                UUID chat3Id = UUID.fromString("c1d2e3f4-a5b6-9012-3456-789012abcdef"); // Example UUID - replace with your own
                Chat chat3 = createChat(chatRepository, chat3Id, "group");


                // --- Create Participants ---
                createParticipant(participantRepository, chat1Id, userA.getId());
                createParticipant(participantRepository, chat1Id, userB.getId());

                createParticipant(participantRepository, chat2Id, userA.getId());
                createParticipant(participantRepository, chat2Id, userC.getId());

                createParticipant(participantRepository, chat3Id, userA.getId());
                createParticipant(participantRepository, chat3Id, userB.getId());
                createParticipant(participantRepository, chat3Id, userC.getId());


                System.out.println("Dummy data created successfully!");
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

    private void createParticipant(ParticipantRepository participantRepository, UUID chatId, UUID userId) {
        ParticipantKey participantKey = new ParticipantKey(chatId, userId, Instant.now());
        Participant participant = new Participant(participantKey);
        participantRepository.save(participant);
    }


}
