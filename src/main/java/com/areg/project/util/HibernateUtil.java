/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.util;

import com.areg.project.entities.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HibernateUtil {

    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);

    private static SessionFactory sessionFactory;
    private HibernateUtil() {}
    public static SessionFactory getSessionFactory() {

        if (sessionFactory == null) {
            try {
                var configuration = new Configuration().configure();
                configuration.addAnnotatedClass(User.class);
                var builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
            } catch (Exception e) {
                //  FIXME !! Error message
                logger.error("Could not instantiate Session Factory !");
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}
