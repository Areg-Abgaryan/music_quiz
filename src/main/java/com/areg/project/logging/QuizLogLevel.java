/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.logging;

import lombok.Getter;

@Getter
public enum QuizLogLevel {

    TRACE(0), DEBUG(1), INFO(2), WARN(3), ERROR(4), FATAL(5);

    private final int level;

    QuizLogLevel(int level) { this.level = level; }
}