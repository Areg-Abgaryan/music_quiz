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

    //  FIXME !! Close access to these fields
    private final Map<MusicAlbum, MusicArtist> albumToArtistMap;
    private final Map<MusicSong, MusicArtist> songToArtistMap;
    private final Map<MusicArtist, Set<MusicAlbum>> artistToAlbumsMap;

    private MusicDatabase() {
        albumToArtistMap = new HashMap<>();
        songToArtistMap = new HashMap<>();
        artistToAlbumsMap = new HashMap<>();
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
    public Set<MusicArtist> getArtists() {
        return artistToAlbumsMap.keySet();
    }

    public void addAlbumToArtist(MusicAlbum album, MusicArtist artist) {
        if (!album.getArtist().equals(artist.getName())) {
            logger.debug("The album {} is not written by the {}", album, artist);
            return;
        }

        if (! artistToAlbumsMap.containsKey(artist)) {
            Set<MusicAlbum> set = artistToAlbumsMap.computeIfAbsent(artist, k -> new HashSet<>());
            set.add(album);
            return;
        }

        Set<MusicAlbum> albums = artistToAlbumsMap.get(artist);
        albums.add(album);
        artistToAlbumsMap.put(artist, albums);
    }
}
