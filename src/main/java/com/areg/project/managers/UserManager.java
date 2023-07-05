/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.entities.User;
import com.areg.project.utils.HibernateUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

//  FIXME !! Add Roles & Permissions for users then
@Service
public class UserManager {

    private static final Logger logger = LoggerFactory.getLogger(UserManager.class);

    public void createUser(User user) {
        final Session session = HibernateUtils.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
            System.out.println("Successful authentication !");
        } catch (Exception e) {
            logger.error("Error : Could not create user : {}", e.getMessage());
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    //  FIXME !!  Consider making this method generic & moving to a higher level
    public List<User> getAllUsers() {
        final Session session = HibernateUtils.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            final List<User> users = HibernateUtils.getAllItemsInTheTable(session, User.class);
            session.getTransaction().commit();
            return users;
        } catch (Exception e) {
            logger.error("Error : Could not get users : {}", e.getMessage());
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return Collections.emptyList();
    }

    public User getUserByEmail(String email)  {

        final Session session = HibernateUtils.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            final User user = HibernateUtils.getRowByUniqueColumn(session, User.class, "email", email);
            session.getTransaction().commit();
            return user;
        } catch (Exception e) {
            logger.error("Error : Could not get users : {}", e.getMessage());
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return null;
    }

    public void deleteUser(User user) {
        final Session session = HibernateUtils.getSessionFactory().openSession();
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
