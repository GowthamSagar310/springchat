package com.gowthamsagar.springchat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests( req -> req
                .requestMatchers("/", "/login", "/register").permitAll()
                .anyRequest().authenticated());

        // csrf (disabled for now)
        http.csrf(AbstractHttpConfigurer::disable);

        // form login
        http.formLogin( form -> form
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .permitAll());

        // oauth2 login
        http.oauth2Login(Customizer.withDefaults());
        http.oauth2Login( oauth -> oauth.defaultSuccessUrl("/home", true)); // if successful login, take to user's home page

        // logout
        http.logout( logout -> { logout
                .logoutSuccessUrl("/logout");
        });

        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }



}