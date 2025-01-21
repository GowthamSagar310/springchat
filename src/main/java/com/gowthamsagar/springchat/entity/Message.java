package com.gowthamsagar.springchat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.util.UUID;

// cassandra entity

@Table(name = "messages")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @PrimaryKeyColumn
    private MessageKey id;

    @CassandraType(type =  CassandraType.Name.TEXT)
    private String content;

    @CassandraType(type = CassandraType.Name.UUID)
    private UUID senderId;

}
