/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.builders;

import com.areg.project.models.MusicAlbum;
import com.areg.project.models.MusicArtist;
import com.areg.project.MusicDatabase;
import com.areg.project.models.MusicSong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class MusicDatabaseBuilder {

    private static final Logger logger = LoggerFactory.getLogger(MusicDatabaseBuilder.class);
    private final MusicDatabase musicDatabase = MusicDatabase.getMusicDBInstance();

    public void buildMusicDatabase(Set<MusicAlbum> musicAlbums) {

        if (musicAlbums.isEmpty()) {
            logger.debug("No music albums found !");
            return;
        }

        final long startTime = System.currentTimeMillis();

        for (var album : musicAlbums) {
            final var artist = new MusicArtist(album.getArtist());
            musicDatabase.addAlbumToArtist(album, artist);
            musicDatabase.addArtistToAlbum(artist, album);

            for (var song : album.getSongs()) {
                final var tempSong = new MusicSong(song.getName(), artist, song.getDuration());
                musicDatabase.addSongToArtist(tempSong, artist);
            }
        }
        logger.debug("buildMusicDatabase takes {} milliseconds", + (System.currentTimeMillis() - startTime));
    }
}
