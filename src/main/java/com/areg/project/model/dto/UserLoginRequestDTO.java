/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserLoginRequestDTO {

    @JsonProperty("username")
    @NotBlank private String username;

    @JsonProperty("password")
    @NotBlank private String password;
}