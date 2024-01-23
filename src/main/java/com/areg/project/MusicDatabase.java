/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project;

import com.areg.project.model.MusicAlbum;
import com.areg.project.model.MusicArtist;
import com.areg.project.model.MusicSong;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MusicDatabase {

    private static final Logger logger = LoggerFactory.getLogger(MusicDatabase.class);
    private static MusicDatabase instance;

    private final Map<MusicAlbum, MusicArtist> albumToArtistMap;
    private final Map<MusicSong, MusicArtist> songToArtistMap;
    @Getter private final Map<MusicArtist, Set<MusicAlbum>> artistToAlbumsMap;

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
        if (album == null || artist == null) {
            logger.info("Unexpected null value for album or artist.");
            return;
        }
        if (! album.getArtist().equals(artist.getName())) {
            logger.debug("The album {} is not written by the {}.", album, artist);
            return;
        }
        albumToArtistMap.put(album, artist);
    }

    public void addSongToArtist(MusicSong song, MusicArtist artist) {
        if (song == null || artist == null) {
            logger.info("Unexpected null value for song or artist.");
            return;
        }
        if (! song.getArtist().getName().equals(artist.getName())) {
            logger.debug("The song {} is not written by the {}.", song, artist);
            return;
        }
        songToArtistMap.put(song, artist);
    }

    public void addArtistToAlbum(MusicArtist artist, MusicAlbum album) {
        if (! album.getArtist().equals(artist.getName())) {
            logger.debug("The album {} is not written by the {}.", album, artist);
            return;
        }

        if (! artistToAlbumsMap.containsKey(artist)) {
            final Set<MusicAlbum> set = artistToAlbumsMap.computeIfAbsent(artist, k -> new HashSet<>());
            set.add(album);
            return;
        }

        final Set<MusicAlbum> albums = artistToAlbumsMap.get(artist);
        albums.add(album);
        artistToAlbumsMap.put(artist, albums);
    }
}
