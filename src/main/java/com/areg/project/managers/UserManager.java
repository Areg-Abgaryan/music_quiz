/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.entities.User;
import com.areg.project.util.HibernateUtil;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

//  FIXME !!    Change package name
//  FIXME !!    Add other methods
@Service
public class UserManager {

    private static final Logger logger = LoggerFactory.getLogger(UserManager.class);

    public static void createUser(User user) {
        final Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Error : Could not create user : {}", e.getMessage());
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    public static void deleteUser(User user) {
        final Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.remove(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Error : Could not delete user : {}", e.getMessage());
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }
}
