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

        final var refreshTokenDto = new RefreshTokenResponseDTO();
        refreshTokenDto.setRefreshToken(refreshTokenEntity.getRefreshToken());
        refreshTokenDto.setExpirationDate(refreshTokenEntity.getExpirationDate());
        return refreshTokenDto;
    }

    public RefreshTokenEntity fromDTOToEntity(RefreshTokenResponseDTO refreshTokenDto) {
        if (refreshTokenDto == null) {
            return null;
        }

        final var refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setRefreshToken(refreshTokenDto.getRefreshToken());
        refreshTokenEntity.setExpirationDate(refreshTokenDto.getExpirationDate());
        return refreshTokenEntity;
    }
}