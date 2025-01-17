package com.gowthamsagar.springchat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;

import java.util.UUID;

// cassandra entity

@Entity
@Table(name = "messages")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @Id
    @PrimaryKey
    private MessageKey id;

    @CassandraType(type =  CassandraType.Name.TEXT)
    private String content;

    @Column(name = "sender_id")
    @CassandraType(type = CassandraType.Name.UUID)
    private UUID senderId;

}
