/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.service.hibernate;

import com.areg.project.logging.QuizLogMachine;
import com.areg.project.model.entity.UserEntity;
import com.areg.project.util.HibernateUtils;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserServiceHibernate {

    private static final QuizLogMachine logMachine = new QuizLogMachine(UserServiceHibernate.class);

    public void updateUserPassword(UserEntity userEntity, String salt, String newEncryptedPassword) {
        final Session session = HibernateUtils.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            userEntity.setPassword(newEncryptedPassword);
            userEntity.setSalt(salt);
            session.merge(userEntity);
            session.getTransaction().commit();
            logMachine.info("Successfully updated userEntity {} password !", userEntity.getUsername());
            System.out.println("Successfully updated password !");
        } catch (Exception e) {
            logMachine.error("Error: Could not update the password of userEntity " + userEntity.getUsername(), e.getMessage());
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
}

    public List<UserEntity> getAllUsers() {
        final Session session = HibernateUtils.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            final List<UserEntity> userEntities = HibernateUtils.getAllItemsInTheTable(session, UserEntity.class);
            session.getTransaction().commit();
            return userEntities;
        } catch (Exception e) {
            logMachine.error("Error : Could not get users : {}", e.getMessage());
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return Collections.emptyList();
    }

    public UserEntity getUserByEmail(String email)  {

        final Session session = HibernateUtils.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            final UserEntity userEntity = HibernateUtils.getRowByUniqueColumn(session, UserEntity.class, "email", email);
            session.getTransaction().commit();
            return userEntity;
        } catch (Exception e) {
            logMachine.error("Error : Could not get user : {}", e.getMessage());
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return null;
    }
}
