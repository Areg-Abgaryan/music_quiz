/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.service.jpa;

import com.areg.project.model.entity.UserEntity;
import com.areg.project.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public UserEntity findUserById(UUID id) {
        return userRepository.findUserEntityByExternalId(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("User with id '" + id.toString() + "' not found")
                );
    }

    public UserEntity findUserByUsername(String username) throws EntityNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new EntityNotFoundException("User with username '" + username + "' not found")
                );
    }
}