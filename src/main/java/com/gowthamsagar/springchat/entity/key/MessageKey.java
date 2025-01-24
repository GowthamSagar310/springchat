package com.gowthamsagar.springchat.entity.key;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;


// why add Serializable
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyClass
public class MessageKey implements Serializable {

    @PrimaryKeyColumn(name = "message_id", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    @CassandraType(type = CassandraType.Name.UUID)
    private UUID messageId;

    @PrimaryKeyColumn(name = "chat_id", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = CassandraType.Name.UUID)
    private UUID chatId;

    @PrimaryKeyColumn(name = "created_at", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private Instant createdAt;


}
