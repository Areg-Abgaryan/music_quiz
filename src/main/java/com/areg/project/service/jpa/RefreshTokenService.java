/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.service.jpa;

import com.areg.project.model.entity.RefreshTokenEntity;
import com.areg.project.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshTokenEntity createToken(RefreshTokenEntity refreshTokenEntity) {
        return refreshTokenRepository.save(refreshTokenEntity);
    }
}
