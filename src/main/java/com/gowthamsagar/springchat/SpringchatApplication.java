package com.gowthamsagar.springchat;

import com.gowthamsagar.springchat.entity.ChatUser;
import com.gowthamsagar.springchat.repository.ChatUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
public class SpringchatApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringchatApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(ChatUserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if(!userRepository.findAll().iterator().hasNext()){
                ChatUser user = new ChatUser();
                user.setUsername("admin");
                user.setEmail("test@test.com");
                user.setPassword(passwordEncoder.encode("dummy"));
                user.setRole("USER");
                userRepository.save(user);
            }
        };
    }

}
