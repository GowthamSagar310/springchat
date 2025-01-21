package com.gowthamsagar.springchat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.time.Instant;
import java.util.UUID;

@Table(name = "chats")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Chat {

    @PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = CassandraType.Name.UUID)
    private UUID id;

    @Column
    private String type;

    // UTC (Universal Coordinated Time)
    // Instant.now() gives current timestamp, which is zone-agnostic.
    // can be converted into any timezone, if needed.

    // can also ZonedDateTime -> can be used but adds complexities during conversions.
    // LocalDateTime -> causes conflicts if the users are in different time zones.

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    // for easy inbox population
    @Column(name = "last_message_content")
    @CassandraType(type =  CassandraType.Name.TEXT)
    private String lastMessageContent;

    @Column(name = "last_message_sender_id")
    @CassandraType(type =  CassandraType.Name.UUID)
    private UUID lastMessageSenderId;

    @Column(name = "last_message_timestamp")
    private Instant lastMessageTimestamp;



}
