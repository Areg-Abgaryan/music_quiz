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

        //  FIXME !! Refactor so that we don't get and add, write add methods
        for (var album : musicAlbums) {

            final var artist = new MusicArtist(album.getArtist());
            musicDatabase.getAlbumToArtistMap().put(album, artist);
            musicDatabase.addAlbumToArtist(album, artist);

            for (var song : album.getSongs()) {
                final var tempSong = new MusicSong(song.getName(), artist, song.getDuration());
                musicDatabase.getSongToArtistMap().put(tempSong, artist);
            }
        }
        logger.debug("buildMusicDatabase takes {} milliseconds", + (System.currentTimeMillis() - startTime));
    }
}
