/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.converter;

import com.areg.project.model.dto.UserDTO;
import com.areg.project.model.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserResultConverter {

    public Set<UserDTO> fromEntityToDTO(Set<UserEntity> userEntitySet) {

        if (userEntitySet == null || userEntitySet.isEmpty()) {
            return Collections.emptySet();
        }

        return userEntitySet.stream().map(this::fromEntityToDTO).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public UserDTO fromEntityToDTO(UserEntity userEntity) {

        if (userEntity == null) {
            return null;
        }

        final var userDTO = new UserDTO();
        userDTO.setId(userEntity.getExternalId());
        userDTO.setUsername(userEntity.getUsername());
        userDTO.setFirstName(userEntity.getFirstName());
        userDTO.setLastName(userEntity.getLastName());
        userDTO.setPassword(userEntity.getPassword());
        userDTO.setEmail(userEntity.getEmail());
        return userDTO;
    }

    public UserEntity fromDTOToEntity(UserDTO userDTO) {

        if (userDTO == null) {
            return null;
        }

        final var userEntity = new UserEntity();
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setFirstName(userDTO.getFirstName());
        userEntity.setLastName(userDTO.getLastName());
        userEntity.setEmail(userDTO.getEmail());
        return userEntity;
    }
}
