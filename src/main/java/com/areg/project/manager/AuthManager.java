/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.manager;

import com.areg.project.converter.RefreshTokenResultConverter;
import com.areg.project.model.dto.RefreshTokenRequestDTO;
import com.areg.project.model.dto.RefreshTokenResponseDTO;
import com.areg.project.model.dto.UserDTO;
import com.areg.project.model.entity.RefreshTokenEntity;
import com.areg.project.model.entity.UserEntity;
import com.areg.project.repository.RefreshTokenRepository;
import com.areg.project.service.jpa.RefreshTokenService;
import com.areg.project.service.jpa.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
public class AuthManager {

    private final long RefreshTokenDurationMs;

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenResultConverter refreshTokenConverter;


    @Autowired
    public AuthManager(UserService userService, RefreshTokenService refreshTokenService,
                       RefreshTokenRepository refreshTokenRepository, RefreshTokenResultConverter refreshTokenConverter,
                       @Value("${refresh.expired}") long expirationMs) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenConverter = refreshTokenConverter;
        this.refreshTokenRepository = refreshTokenRepository;
        this.RefreshTokenDurationMs = expirationMs;
    }

    //  Creates refresh token
    public RefreshTokenResponseDTO createRefreshToken(UserDTO userDTO) {
        final var refreshToken = new RefreshTokenResponseDTO();
        refreshToken.setExpirationDate(Instant.now().plusMillis(RefreshTokenDurationMs));
        refreshToken.setRefreshToken(UUID.randomUUID());
        final RefreshTokenEntity saved = refreshTokenService.createToken(refreshTokenConverter.fromDTOToEntity(refreshToken));
        return refreshTokenConverter.fromEntityToDTO(saved);
    }

    //  Returns true when token expired
    public boolean verifyRefreshTokenExpiration(RefreshTokenEntity token) {
        return Instant.now().isAfter(token.getExpirationDate());
    }

    //  FIXME !! Refactor this
    public Optional<RefreshTokenEntity> refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO) {
        final UserEntity userEntity = userService.findUserById(refreshTokenRequestDTO.getUserExternalId());
        return refreshTokenRepository.
                findByUserEntityAndRefreshToken(userEntity, refreshTokenRequestDTO.getRefreshToken());
    }
}
