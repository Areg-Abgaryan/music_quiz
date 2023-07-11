/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.entities;

import com.areg.project.QuizDifficulty;
import com.areg.project.models.MusicSong;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Table(name = "albums", schema = "public")
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class Album {

    //  FIXME !! Fix relations in this table

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String artist;

    @Column(nullable = false)
    private int releaseYear;

    @Column(nullable = false)
    private int numberOfSongs;

    @Column(nullable = false)
    private String totalLength;

    @Column(nullable = false)
    private ArrayList<MusicSong> songs;

    @Setter
    @Column(nullable = false)
    private QuizDifficulty difficulty;
}
