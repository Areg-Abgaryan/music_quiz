/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.entities.Artist;
import com.areg.project.utils.HibernateUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ArtistManager {

    private static final Logger logger = LoggerFactory.getLogger(ArtistManager.class);

    public void createArtist(Artist artist) {
        final Session session = HibernateUtils.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.persist(artist);
            session.getTransaction().commit();
            logger.info("Successfully created artist {}", artist.getName());
        } catch (Exception e) {
            logger.error("Error : Could not create artist : {}", e.getMessage());
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }
}
