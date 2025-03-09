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

import java.time.Instant;
import java.util.UUID;

@PrimaryKeyClass
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantKey {

    // query 1: participants involved in chatId
    // query 2: all the chats, userId is involved in
    // both of them cannot be done efficiently with a single query.
    // cassandra won't allow two partition keys in a single table.
    // so we need to create a separate table for each query. (participants_by_chat, participants_by_user)
    // de-normalization.
    // is there a way to do this with a single table, without "Allow Filtering"?
    // no, because the partition key is the only way to filter the data.
    // TODO: change this

    @PrimaryKeyColumn(name = "chat_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = CassandraType.Name.UUID)
    private UUID chatId;

    @PrimaryKeyColumn(name = "user_id", ordinal = 2, type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = CassandraType.Name.UUID)
    private UUID userId;

    @PrimaryKeyColumn(name = "created_at", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private Instant createdAt;

}
