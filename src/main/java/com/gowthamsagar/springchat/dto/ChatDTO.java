package com.gowthamsagar.springchat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {

    private UUID id;
    private String senderName;
    private String lastMessageContent;
    private Instant lastMessageTimestamp;

}
