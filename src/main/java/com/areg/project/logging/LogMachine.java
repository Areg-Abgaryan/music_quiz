/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogMachine {

    private final Logger logger;

    // Constructor to initialize the SLF4J logger
    public LogMachine(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    public void trace(String message) {
        logger.trace(message);
    }

    public void debug(String message) {
        logger.debug(message);
    }

    public void info(String message) {
        logger.info(message);
    }

    public void warn(String message) {
        logger.warn(message);
    }

    public void error(String message) {
        logger.error(message);
    }

    public void log(LogLevel level, String message) {
        switch (level) {
            case TRACE -> logger.trace(message);
            case DEBUG -> logger.debug(message);
            case INFO -> logger.info(message);
            case WARN -> logger.warn(message);
            case ERROR -> logger.error(message);
            default -> throw new IllegalArgumentException("Unsupported log level: " + level);
        }
    }

    //  FIXME !! See whether i need Log Level
}
