package com.gowthamsagar.springchat.config;

import com.gowthamsagar.springchat.security.CustomAuthenticationFailureHandler;
import com.gowthamsagar.springchat.security.securityservice.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomAuthenticationFailureHandler customAuthenticationFailureHandler, CustomOAuth2UserService customOAuth2UserService) throws Exception {

        http.authorizeHttpRequests( req -> req
                .requestMatchers("/", "/sign-up", "/register", "/login", "/invalid-session").permitAll()
                .anyRequest().authenticated());

        // csrf (disabled for now)
        http.csrf(AbstractHttpConfigurer::disable);

        // form login
        http.formLogin( form -> form
                .loginPage("/")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/home", true)
                .failureHandler(customAuthenticationFailureHandler)
                .permitAll());

        // oauth2 login
        // http.oauth2Login(Customizer.withDefaults());
        http.oauth2Login( oauth -> oauth
                .loginPage("/")
                .userInfoEndpoint(
                        userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOAuth2UserService)
                )
                .defaultSuccessUrl("/home", true) // if successful login, take to user's home page
        );

        // logout
        http.logout( logout -> { logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll();
        });

        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }



}