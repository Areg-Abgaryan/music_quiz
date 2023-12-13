/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.service.hibernate;

import com.areg.project.logging.QuizLogMachine;
import com.areg.project.model.entity.AlbumEntity;
import com.areg.project.model.entity.ArtistEntity;
import com.areg.project.model.entity.SongEntity;
import com.areg.project.util.HibernateUtils;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SongServiceHibernate {

    private static final QuizLogMachine logMachine = new QuizLogMachine(SongServiceHibernate.class);

    public void createSong(SongEntity songEntity, AlbumEntity albumEntity, ArtistEntity artistEntity) {
        final Session session = HibernateUtils.getSessionFactory().openSession();
        try {
            songEntity.setArtistEntity(artistEntity);
            songEntity.setAlbumEntity(albumEntity);
            session.beginTransaction();
            session.save(songEntity);
            session.getTransaction().commit();
            logMachine.debug("Successfully created songEntity {}", songEntity.getName());
        } catch (Exception e) {
            logMachine.error("Error: Could not create songEntity : {}", e.getMessage());
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    public void createSongs(Collection<SongEntity> songEntities) {
        final Session session = HibernateUtils.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            songEntities.forEach(session::save);
            session.getTransaction().commit();
            logMachine.debug("Successfully created songEntities");
        } catch (Exception e) {
            logMachine.error("Error: Could not create songEntities : {}", e.getMessage());
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }
}
