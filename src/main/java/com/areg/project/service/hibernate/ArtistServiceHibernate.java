/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.service.hibernate;

import com.areg.project.logging.QuizLogMachine;
import com.areg.project.model.entity.ArtistEntity;
import com.areg.project.util.HibernateUtils;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

@Service
public class ArtistServiceHibernate {

    private static final QuizLogMachine logMachine = new QuizLogMachine(ArtistServiceHibernate.class);

    public void createArtist(ArtistEntity artistEntity) {
        final Session session = HibernateUtils.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.persist(artistEntity);
            session.getTransaction().commit();
            logMachine.debug("Successfully created artistEntity {}", artistEntity.getName());
        } catch (Exception e) {
            logMachine.error("Error : Could not create artistEntity : {}", e.getMessage());
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }
}
