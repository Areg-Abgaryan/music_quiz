/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class UserLoginResponseDTO {

    @NotBlank private String firstName;

    @NotBlank private String lastName;

    @NotBlank private TokenDTO token;

    @NotBlank private RefreshTokenResponseDTO refreshToken;
}