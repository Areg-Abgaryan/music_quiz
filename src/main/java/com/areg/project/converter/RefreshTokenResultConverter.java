/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.converter;

import com.areg.project.model.dto.RefreshTokenResponseDTO;
import com.areg.project.model.entity.RefreshTokenEntity;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenResultConverter {

    public RefreshTokenResponseDTO fromEntityToDTO(RefreshTokenEntity refreshTokenEntity) {
        if (refreshTokenEntity == null) {
            return null;
        }
        final var refreshTokenDTO = new RefreshTokenResponseDTO();
        refreshTokenDTO.setRefreshToken(refreshTokenEntity.getRefreshToken());
        refreshTokenDTO.setExpirationDate(refreshTokenEntity.getExpirationDate());
        return refreshTokenDTO;
    }

    public RefreshTokenEntity fromDTOToEntity(RefreshTokenResponseDTO refreshTokenDTO) {
        if (refreshTokenDTO == null) {
            return null;
        }
        final var refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setRefreshToken(refreshTokenDTO.getRefreshToken());
        refreshTokenEntity.setExpirationDate(refreshTokenDTO.getExpirationDate());
        return refreshTokenEntity;
    }
}