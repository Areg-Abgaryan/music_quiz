/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.model.entity;

import com.areg.project.QuizDifficulty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Table(name = "t_album", schema = "public")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class AlbumEntity {

    //  FIXME !! Consider changing heavy objects to Strings, like albumName, songName
    //  FIXME !! Change id column types
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id")
    private Long albumId;

    @Column(nullable = false, name = "name")
    private String name;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH }, optional = false)
    @JoinColumn(name = "fk_album_artist", referencedColumnName = "artist_id", nullable = false)
    private ArtistEntity artistEntity;

    @Column(nullable = false, name = "release_year")
    private short releaseYear;

    @Column(nullable = false, name = "number_of_songs")
    private byte numberOfSongs;

    @Column(nullable = false, name = "total_length")
    private String totalLength;

    @Column(name = "songEntities")
    @OneToMany(mappedBy = "album") //, orphanRemoval = true, fetch = FetchType.LAZY,
    //cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE })
    private List<SongEntity> songEntities;

    @Setter
    @Column(nullable = false, name = "difficulty")
    private QuizDifficulty difficulty;

    public AlbumEntity(String name, ArtistEntity artistEntity, short releaseYear, byte numberOfSongs, String totalLength,
                       List<SongEntity> songEntities, QuizDifficulty difficulty) {
        this.name = name;
        this.artistEntity = artistEntity;
        this.releaseYear = releaseYear;
        this.numberOfSongs = numberOfSongs;
        this.totalLength = totalLength;
        this.songEntities = songEntities;
        this.difficulty = difficulty;
    }
}
