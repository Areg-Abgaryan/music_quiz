/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.util;

import com.areg.project.entities.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HibernateUtil {

    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);

    private static SessionFactory sessionFactory;
    public static SessionFactory getSessionFactory() {

        if (sessionFactory == null) {
            try {
                var configuration = new Configuration().configure();
                configuration.addAnnotatedClass(User.class);
                var builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
            } catch (Exception e) {
                logger.error("Error : Could not instantiate Session Factory !");
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    public static <T> List<T> getAllItemsInTheTable(Class<T> type, Session session) {
        final CriteriaBuilder builder = session.getCriteriaBuilder();
        final CriteriaQuery<T> criteria = builder.createQuery(type);
        criteria.from(type);
        return session.createQuery(criteria).getResultList();
    }
}
