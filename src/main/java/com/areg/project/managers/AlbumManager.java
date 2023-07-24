/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.entities.Album;
import com.areg.project.entities.Artist;
import com.areg.project.utils.HibernateUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AlbumManager {

    private static final Logger logger = LoggerFactory.getLogger(AlbumManager.class);

    public void createAlbum(Album album, Artist artist) {
        final Session session = HibernateUtils.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            album.setArtist(artist);
            session.save(album);
            session.getTransaction().commit();
            logger.debug("Successfully created album {}", album.getName());
        } catch (Exception e) {
            logger.error("Error : Could not create album : {}", e.getMessage());
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }
}
