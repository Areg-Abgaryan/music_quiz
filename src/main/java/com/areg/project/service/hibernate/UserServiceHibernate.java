/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.service.hibernate;

import com.areg.project.logging.QuizLogMachine;
import com.areg.project.model.entity.UserEntity;
import com.areg.project.util.HibernateUtils;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

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
}
