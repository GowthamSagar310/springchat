package com.gowthamsagar.springchat.entity;

import com.gowthamsagar.springchat.entity.key.ParticipantKey;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;

// cassandra entity

@Table(name = "participants")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Participant {

    @PrimaryKey
    private ParticipantKey id;

}
