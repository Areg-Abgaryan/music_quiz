/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.builders;

import com.areg.project.QuizDifficulty;
import com.areg.project.models.MusicAlbum;
import com.areg.project.models.MusicArtist;
import com.areg.project.MusicDatabase;
import com.areg.project.models.MusicSong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.IntStream;

@Service
public class MusicDatabaseBuilder {

    private static final Logger logger = LoggerFactory.getLogger(MusicDatabaseBuilder.class);
    private final MusicDatabase musicDatabase = MusicDatabase.getMusicDBInstance();

    public void buildMusicDatabase(Set<MusicAlbum> musicAlbums) {

        if (musicAlbums.isEmpty()) {
            logger.info("No music albums found !");
            return;
        }

        final long startTime = System.currentTimeMillis();

        for (var album : musicAlbums) {
            album.setDifficulty(calculateAlbumDifficulty(album));

            final var artist = new MusicArtist(album.getArtist());
            musicDatabase.addAlbumToArtist(album, artist);
            musicDatabase.addArtistToAlbum(artist, album);

            for (var song : album.getSongs()) {
                final var tempSong = new MusicSong(song.getName(), artist, album, song.getDuration(), song.getDifficulty());
                musicDatabase.addSongToArtist(tempSong, artist);
            }
        }
        logger.info("buildMusicDatabase takes {} milliseconds.", + (System.currentTimeMillis() - startTime));
    }

    private QuizDifficulty calculateAlbumDifficulty(MusicAlbum album) {

        final byte songs = album.getNumberOfSongs();
        final byte totalDifficultySum = (byte) IntStream.range(0, songs).map(i -> album.getSongs().get(i).getDifficulty()).sum();
        final double averageDifficulty = (double) totalDifficultySum / songs;

        if (averageDifficulty > 2) {
            return QuizDifficulty.HARD;
        } else if (averageDifficulty > 1) {
            return QuizDifficulty.MEDIUM;
        } else {
            return QuizDifficulty.EASY;
        }
    }
}
