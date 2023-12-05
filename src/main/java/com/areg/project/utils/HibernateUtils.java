/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.utils;

import com.areg.project.entities.Album;
import com.areg.project.entities.Artist;
import com.areg.project.entities.Song;
import com.areg.project.entities.User;
import com.areg.project.logging.LogMachine;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HibernateUtils {

    private static final LogMachine logMachine = new LogMachine(HibernateUtils.class);
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {

        if (sessionFactory == null) {
            try {
                final var configuration = new Configuration().configure();
                configuration.addAnnotatedClass(Artist.class).addAnnotatedClass(Album.class).addAnnotatedClass(Song.class)
                        .addAnnotatedClass(User.class);
                final var builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
            } catch (HibernateException e) {
                logMachine.error("Error : Could not instantiate Session Factory !");
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    public static <T> List<T> getAllItemsInTheTable(Session session, Class<T> type) {
        final CriteriaBuilder builder = session.getCriteriaBuilder();
        final CriteriaQuery<T> criteria = builder.createQuery(type);
        criteria.from(type);
        return session.createQuery(criteria).getResultList();
    }

    public static <T> T getRowByUniqueColumn(Session session, Class<T> type, String fieldName, Object fieldValue) {
        final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        final CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(type);
        final Root<T> root = criteriaQuery.from(type);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(fieldName), fieldValue));
        //  Here we can write an HQL query
        final Query<T> query = session.createQuery(criteriaQuery);
        return query.getSingleResult();
    }
}
