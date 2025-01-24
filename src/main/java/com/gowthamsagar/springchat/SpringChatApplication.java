package com.gowthamsagar.springchat;

import com.gowthamsagar.springchat.entity.ChatUser;
import com.gowthamsagar.springchat.repository.ChatUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
public class SpringChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringChatApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(ChatUserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            ChatUser admin = ChatUser.builder()
                    .username("admin")
                    .email("admin@test.com")
                    .password(passwordEncoder.encode("dummy"))
                    .role("USER")
                    .build();

            ChatUser test1 = ChatUser.builder()
                    .username("test1")
                    .email("test1@test.com")
                    .password(passwordEncoder.encode("dummy"))
                    .role("USER")
                    .build();

            ChatUser test2 = ChatUser.builder()
                    .username("test2")
                    .email("test2@test.com")
                    .password(passwordEncoder.encode("dummy"))
                    .role("USER")
                    .build();

            ChatUser deepika = ChatUser.builder()
                    .username("deepika")
                    .email("deepika@test.com")
                    .password(passwordEncoder.encode("deepika"))
                    .role("USER")
                    .build();

            userRepository.save(admin);
            userRepository.save(test1);
            userRepository.save(test2);
            userRepository.save(deepika);
        };
    }
}
