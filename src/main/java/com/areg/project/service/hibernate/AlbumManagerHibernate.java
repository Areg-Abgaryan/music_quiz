/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.service.hibernate;

import com.areg.project.logging.QuizLogMachine;
import com.areg.project.model.entity.AlbumEntity;
import com.areg.project.model.entity.ArtistEntity;
import com.areg.project.util.HibernateUtils;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

@Service
public class AlbumManagerHibernate {

    private static final QuizLogMachine logMachine = new QuizLogMachine(AlbumManagerHibernate.class);

    public void createAlbum(AlbumEntity albumEntity, ArtistEntity artistEntity) {
        final Session session = HibernateUtils.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            albumEntity.setArtistEntity(artistEntity);
            session.save(albumEntity);
            session.getTransaction().commit();
            logMachine.debug("Successfully created albumEntity {}", albumEntity.getName());
        } catch (Exception e) {
            logMachine.error("Error : Could not create albumEntity : {}", e.getMessage());
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }
}
