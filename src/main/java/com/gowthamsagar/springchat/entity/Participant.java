package com.gowthamsagar.springchat.entity;

import com.gowthamsagar.springchat.entity.key.ParticipantKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

// cassandra entity

@Table("participants")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Participant {

    @PrimaryKey
    private ParticipantKey id;

}
