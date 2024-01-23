/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.repository;

import com.areg.project.model.entity.UserEntity;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByExternalId(UUID userId);
}