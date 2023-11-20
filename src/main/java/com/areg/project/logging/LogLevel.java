/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.logging;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LogLevel {
    TRACE, DEBUG, INFO, WARN, ERROR
}