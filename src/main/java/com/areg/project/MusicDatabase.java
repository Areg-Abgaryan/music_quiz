/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project;

import com.areg.project.models.MusicAlbum;
import com.areg.project.models.MusicArtist;
import com.areg.project.models.MusicSong;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
public class MusicDatabase {

    private static final Logger logger = LoggerFactory.getLogger(MusicDatabase.class);
    private static MusicDatabase instance;

    private final Map<MusicAlbum, MusicArtist> albumToArtistMap;
    private final Map<MusicSong, MusicArtist> songToArtistMap;
    private final Set<MusicArtist> artists;

    private MusicDatabase() {
        albumToArtistMap = new HashMap<>();
        songToArtistMap = new HashMap<>();
        artists = new HashSet<>();
    }

    public static MusicDatabase getMusicDBInstance() {
        if (instance == null) {
            synchronized (MusicDatabase.class) {
                if (instance == null) {
                    instance = new MusicDatabase();
                }
            }
        }
        return instance;
    }

    public Set<MusicAlbum> getAlbums() {
        return albumToArtistMap.keySet();
    }

    public Set<MusicSong> getSongs() {
        return songToArtistMap.keySet();
    }
}
