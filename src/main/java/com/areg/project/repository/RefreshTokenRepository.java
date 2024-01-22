/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.repository;

import com.areg.project.model.entity.RefreshTokenEntity;
import com.areg.project.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByUserEntityAndRefreshToken(UserEntity userEntity, UUID refreshToken);
}
