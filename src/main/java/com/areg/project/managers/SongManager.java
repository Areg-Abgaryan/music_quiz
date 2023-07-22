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

@Service
public class SongManager {

    private static final Logger logger = LoggerFactory.getLogger(SongManager.class);

    public void createSong(Song song, Album album, Artist artist) {
        final Session session = HibernateUtils.getSessionFactory().openSession();
        try {
            song.setArtist(artist);
            song.setAlbum(album);
            session.beginTransaction();
            session.persist(song);
            session.getTransaction().commit();
            logger.debug("Successfully created song {}", song.getName());
        } catch (Exception e) {
            logger.error("Error : Could not create song : {}", e.getMessage());
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }
}
