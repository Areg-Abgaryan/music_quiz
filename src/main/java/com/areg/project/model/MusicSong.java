/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class MusicSong {
    private String name;
    private MusicArtist artist;
    private MusicAlbum album;
    private String duration;
    private byte difficulty;
}
