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

        final var userDto = new UserDTO();
        userDto.setId(userEntity.getExternalId());
        userDto.setUsername(userEntity.getUsername());
        userDto.setPassword(userEntity.getPassword());
        userDto.setEmail(userEntity.getEmail());
        return userDto;
    }

    public UserEntity fromDTOToEntity(UserDTO userDto) {
        if (userDto == null) {
            return null;
        }

        final var userEntity = new UserEntity();
        userEntity.setEmail(userDto.getEmail());
        userEntity.setUsername(userDto.getUsername());
        userEntity.setPassword(userDto.getPassword());
        return userEntity;
    }
}
