/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.manager;

import com.areg.project.converter.UserResultConverter;
import com.areg.project.model.dto.UserDTO;
import com.areg.project.model.entity.UserEntity;
import com.areg.project.service.jpa.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserManager {

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

    public UserDTO findUserByUsername(String username) {
        final UserEntity userEntity = userService.findUserByUsername(username);
        return userResultConverter.fromEntityToDTO(userEntity);
    }

    public UserDTO findUserById(UUID id) {
        final UserEntity userEntity = userService.findUserById(id);
        return userResultConverter.fromEntityToDTO(userEntity);
    }

    public UserDTO signUp(UserDTO userDTO) {
        final UserEntity userEntity = userResultConverter.fromDTOToEntity(userDTO);
        final String salt = encryptionManager.generateSalt();
        userEntity.setSalt(salt);

        final String encryptedPassword = encryptionManager.encrypt(userDTO.getPassword(), salt);
        userEntity.setPassword(encryptedPassword);

        final UserEntity savedUserEntity = userService.createUser(userEntity);
        return userResultConverter.fromEntityToDTO(savedUserEntity);
    }
}
