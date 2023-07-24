/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "songs", schema = "public")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "songId")
    private Long songId;

    @Column(nullable = false)
    private String name;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH }, optional = false)
    @JoinColumn(name = "fk_song_artist", referencedColumnName = "artistId", nullable = false)
    private Artist artist;

    @Setter
    @JoinColumn(name = "fk_song_album", referencedColumnName = "albumId")
    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH }, optional = false)
    private Album album;

    @Column(nullable = false)
    private String duration;

    @Column(nullable = false)
    private byte difficulty;

    public Song(String name, Artist artist, Album album, String duration, byte difficulty) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.difficulty = difficulty;
    }
}
