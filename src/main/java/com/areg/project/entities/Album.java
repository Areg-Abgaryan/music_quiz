/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.entities;

import com.areg.project.QuizDifficulty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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

@Table(name = "albums", schema = "public")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "albumId")
    private Long albumId;

    @Column(nullable = false)
    private String name;

    //  FIXME !! Consider changing heavy objects to Strings, like albumName, songName

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH }, optional = false)
    @JoinColumn(name = "fk_album_artist", referencedColumnName = "artistId", nullable = false)
    private Artist artist;

    @Column(nullable = false)
    private short releaseYear;

    @Column(nullable = false)
    private byte numberOfSongs;

    @Column(nullable = false)
    private String totalLength;

    @Column
    //  FIXME !!    @Column(nullable = false)
    @OneToMany(mappedBy = "album") //, orphanRemoval = true, fetch = FetchType.LAZY,
            //cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE })
    private List<Song> songs;

    @Setter
    @Column(nullable = false)
    private QuizDifficulty difficulty;

    public Album(String name, Artist artist, short releaseYear, byte numberOfSongs, String totalLength,
                 List<Song> songs, QuizDifficulty difficulty) {
        this.name = name;
        this.artist = artist;
        this.releaseYear = releaseYear;
        this.numberOfSongs = numberOfSongs;
        this.totalLength = totalLength;
        this.songs = songs;
        this.difficulty = difficulty;
    }
}
