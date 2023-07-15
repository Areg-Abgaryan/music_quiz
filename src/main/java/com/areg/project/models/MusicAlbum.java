/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.models;

import com.areg.project.QuizDifficulty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class MusicAlbum {
    private String name;
    private String artist;
    private short releaseYear;
    private byte numberOfSongs;
    private String totalLength;
    private ArrayList<MusicSong> songs;
    @Setter private QuizDifficulty difficulty;
}
