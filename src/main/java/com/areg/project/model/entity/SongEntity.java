/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.model.entity;

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

@Table(name = "t_song", schema = "public")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class SongEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "song_id")
    private Long id;

    @Column(nullable = false, name = "name")
    private String name;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH }, optional = false)
    @JoinColumn(name = "fk_song_artist", referencedColumnName = "artist_id", nullable = false)
    private ArtistEntity artistEntity;

    @Setter
    @JoinColumn(name = "fk_song_album", referencedColumnName = "album_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH }, optional = false)
    private AlbumEntity albumEntity;

    @Column(nullable = false, name = "duration")
    private String duration;

    @Column(nullable = false, name = "difficulty")
    private byte difficulty;

    public SongEntity(String name, ArtistEntity artistEntity, AlbumEntity albumEntity, String duration, byte difficulty) {
        this.name = name;
        this.artistEntity = artistEntity;
        this.albumEntity = albumEntity;
        this.duration = duration;
        this.difficulty = difficulty;
    }
}
