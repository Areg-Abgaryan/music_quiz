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

@Table(name = "t_song", schema = "public")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SongEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(nullable = false, name = "name")
    private String name;

    //   FIXME !!   Fix all entities here
   /* @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH }, optional = false)
    @JoinColumn(name = "fk_song_artist", referencedColumnName = "id", nullable = false)
    private ArtistEntity artistEntity;

    @Setter
    @JoinColumn(name = "fk_song_album", referencedColumnName = "id")
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
    }*/
}
