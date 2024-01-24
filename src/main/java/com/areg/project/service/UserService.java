/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.service;

import com.areg.project.logging.QuizLogMachine;
import com.areg.project.model.entity.UserEntity;
import com.areg.project.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final QuizLogMachine logMachine = new QuizLogMachine(UserService.class);
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public UserEntity signUp(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public void updatePassword(UserEntity userEntity, String salt, String newEncryptedPassword) {
        try {
            userEntity.setPassword(newEncryptedPassword);
            userEntity.setSalt(salt);
            userRepository.save(userEntity);
            logMachine.info("Successfully updated password for user " + userEntity.getUsername());
        } catch (DataAccessException e) {
            final String errorMessage = "Error updating password for user " + userEntity.getUsername();
            logMachine.error(errorMessage, e);
        }
    }
}