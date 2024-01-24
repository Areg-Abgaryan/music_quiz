/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.manager;

import com.areg.project.converter.UserResultConverter;
import com.areg.project.logging.QuizLogMachine;
import com.areg.project.model.dto.UserDTO;
import com.areg.project.model.entity.UserEntity;
import com.areg.project.service.UserService;
import com.areg.project.util.UtilMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserManager {

    private final QuizLogMachine logMachine = new QuizLogMachine(UserManager.class);
    private final UserService userService;
    private final UserResultConverter userResultConverter;
    private final EncryptionManager encryptionManager;

    @Autowired
    public UserManager(UserService userService, UserResultConverter userResultConverter,
                       EncryptionManager encryptionManager) {
        this.userService = userService;
        this.userResultConverter = userResultConverter;
        this.encryptionManager = encryptionManager;
    }

    public UserDTO signUp(UserDTO userDTO) {
        final UserEntity userEntity = userResultConverter.fromDTOToEntity(userDTO);
        final String salt = encryptionManager.generateSalt();
        userEntity.setSalt(salt);

        final String encryptedPassword = encryptionManager.encrypt(userDTO.getPassword(), salt);
        userEntity.setPassword(encryptedPassword);

        userEntity.setExternalId(UUID.randomUUID());
        userEntity.setRegistrationDate(UtilMethods.getEpochSeconds());

        final UserEntity savedUserEntity = userService.signUp(userEntity);
        logMachine.info("User " + savedUserEntity.getUsername() + " successfully registered in the system");
        return userResultConverter.fromEntityToDTO(savedUserEntity);
    }

    public List<UserDTO> getAllUsers() {
        return userResultConverter.fromEntityToDTO(userService.getAllUsers());
    }

    public UserDTO getUserByEmail(String email) {
        final Optional<UserEntity> result = userService.getUserByEmail(email);
        if (result.isEmpty()) {
            logMachine.info("Could not find user with this email : " + email);
            return null;
        }
        return userResultConverter.fromEntityToDTO(result.get());
    }

    public void updatePassword(UserDTO user, String salt, String newEncryptedPassword) {
        final UserEntity userEntity = userResultConverter.fromDTOToEntity(user);
        userService.updatePassword(userEntity, salt, newEncryptedPassword);
    }
}
