/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "artists", schema = "public")
@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@ToString
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artistId")
    private Long artistId;

    @Column(unique = true, nullable = false)
    private String name;

    public Artist(String name) {
        this.name = name;
    }
}
