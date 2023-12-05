/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.logging;

import lombok.Getter;

@Getter
public enum LogLevel {

    TRACE(0), DEBUG(1), INFO(2), WARN(3), ERROR(4), FATAL(5);

    private final int level;

    LogLevel(int level) { this.level = level; }
}