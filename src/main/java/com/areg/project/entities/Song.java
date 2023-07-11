/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.entities;

import com.areg.project.models.MusicAlbum;
import com.areg.project.models.MusicArtist;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "songs", schema = "public")
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class Song {

    //  FIXME !! Fix relations in this table

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private MusicArtist artist;

    @Column(nullable = false)
    private MusicAlbum album;

    @Column(nullable = false)
    private String duration;

    @Column(nullable = false)
    private int difficulty;
}
