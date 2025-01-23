package com.gowthamsagar.springchat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

    private UUID chatId;
    private UUID senderId;
    private String content;

    // private String type;  // chat type

}
