/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.entities.Album;
import com.areg.project.entities.Artist;
import com.areg.project.entities.Song;
import com.areg.project.utils.HibernateUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SongManager {

    private static final Logger logger = LoggerFactory.getLogger(SongManager.class);

    public void createSong(Song song, Album album, Artist artist) {
        final Session session = HibernateUtils.getSessionFactory().openSession();
        try {
            song.setArtist(artist);
            song.setAlbum(album);
            session.beginTransaction();
            session.save(song);
            session.getTransaction().commit();
            logger.debug("Successfully created song {}", song.getName());
        } catch (Exception e) {
            logger.error("Error: Could not create song : {}", e.getMessage());
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    public void createSongs(Collection<Song> songs) {
        final Session session = HibernateUtils.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            songs.forEach(session::save);
            session.getTransaction().commit();
            logger.debug("Successfully created songs");
        } catch (Exception e) {
            logger.error("Error: Could not create songs : {}", e.getMessage());
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }
}
