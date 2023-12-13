/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Getter
@Setter
public class RefreshTokenRequestDTO {

    @JsonProperty("refreshToken")
    @NotBlank private UUID refreshToken;

    @JsonProperty("userId")
    @NotBlank private UUID userExternalId;
}
