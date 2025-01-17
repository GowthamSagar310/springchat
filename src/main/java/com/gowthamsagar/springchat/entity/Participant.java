package com.gowthamsagar.springchat.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;

@Table(name = "participants")
@Getter @Setter
@Accessors(chain = true)
@NoArgsConstructor
public class Participant {

    @PrimaryKey
    private ParticipantKey id;

}
