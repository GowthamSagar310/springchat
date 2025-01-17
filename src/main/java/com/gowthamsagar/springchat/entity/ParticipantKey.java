package com.gowthamsagar.springchat.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.util.UUID;

@PrimaryKeyClass
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantKey {

    @PrimaryKeyColumn(name = "chat_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = CassandraType.Name.UUID)
    private UUID chatId;

    @PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.CLUSTERED    )
    @CassandraType(type = CassandraType.Name.UUID)
    private UUID userId;



}
