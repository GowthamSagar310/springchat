package com.gowthamsagar.springchat.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

// cassandra entity
@Table("chats")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Chat {

    @PrimaryKeyColumn(name = "chat_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = CassandraType.Name.UUID)
    private UUID id;

    @Column("type")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String type;

    // UTC (Universal Coordinated Time)
    // Instant.now() gives current timestamp, which is zone-agnostic.
    // can be converted into any timezone, if needed.

    // can also ZonedDateTime -> can be used but adds complexities during conversions.
    // LocalDateTime -> causes conflicts if the users are in different time zones.

    @Column("created_at")
    @CassandraType(type = CassandraType.Name.TIMESTAMP)
    private Instant createdAt;

    // for easy inbox population
    @Column("last_message_content")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String lastMessageContent;

    @Column("last_message_sender_id")
    @CassandraType(type = CassandraType.Name.UUID)
    private UUID lastMessageSenderId;

    @Column("last_message_timestamp")
    @CassandraType(type = CassandraType.Name.TIMESTAMP)
    private Instant lastMessageTimestamp;

}
