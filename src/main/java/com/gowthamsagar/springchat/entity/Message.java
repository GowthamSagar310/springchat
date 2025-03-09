package com.gowthamsagar.springchat.entity;

import com.gowthamsagar.springchat.entity.key.MessageKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

// cassandra entity
@Table("messages")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @PrimaryKeyColumn(name = "message_id", type = PrimaryKeyType.PARTITIONED)
    private MessageKey id;

    @CassandraType(type =  CassandraType.Name.TEXT)
    private String content;

    @CassandraType(type = CassandraType.Name.UUID)
    @Column("sender_id")
    private UUID senderId;

    // why not add the receiverId here itself ?
    // adding receiverId here would work for one-to-one chats, but for group chats, lot of redundancy would be there.
    // it is better to store that information separately.

}
