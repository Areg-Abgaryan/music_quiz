/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.utils;

import com.areg.project.entities.User;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HibernateUtils {

    private static final Logger logger = LoggerFactory.getLogger(HibernateUtils.class);

    private static SessionFactory sessionFactory;
    public static SessionFactory getSessionFactory() {

        if (sessionFactory == null) {
            try {
                var configuration = new Configuration().configure();
                configuration.addAnnotatedClass(User.class);
                var builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
            } catch (HibernateException e) {
                logger.error("Error : Could not instantiate Session Factory !");
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
