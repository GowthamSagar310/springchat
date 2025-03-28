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
public class MessageDTO {

    private UUID chatId;
    private UUID senderId;
    private String message;
    private String username;

    // private String type;  // chat type

}
