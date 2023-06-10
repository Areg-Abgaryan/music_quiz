/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class MusicAlbum {
    private String name;
    private String artist;
    private int releaseYear;
    private int numberOfSongs;
    private String totalLength;
    private ArrayList<MusicSong> songs;
}
