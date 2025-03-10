package com.gowthamsagar.springchat.entity;

import com.gowthamsagar.springchat.entity.key.ChatOfParticipantKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("chats_by_participant")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatOfParticipant {

    @PrimaryKey
    private ChatOfParticipantKey id;

}
