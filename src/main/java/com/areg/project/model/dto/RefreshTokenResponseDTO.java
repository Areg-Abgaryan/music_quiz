/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.model.dto;

import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.UUID;

public class RefreshTokenResponseDTO {

    @NotBlank private Long id;

    @NotBlank private UUID refreshToken;

    @NotBlank private Instant expirationDate;
}
