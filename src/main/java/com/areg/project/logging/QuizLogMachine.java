/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class QuizLogMachine {

    //  FIXME !! Move slf4j logger logs to my custom QuizLogMachine logging everywhere in the code
    //  FIXME !! See whether i need Logging level, and log() method
    //  FIXME !! See whether i can specify this logger output in log4j2.xml file
    //  FIXME !! Add logging possibility with these brackets mode {}, something ...

    private final Logger logger;

    // Constructor to initialize the SLF4J logger
    public QuizLogMachine(Class<?> clazz) {
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

    public void trace(String message, Object... args) {
        if (logger.isTraceEnabled()) {
            logger.warn(String.format(message, args));
        }
    }

    public void debug(String message, Object... args) {
        if (logger.isDebugEnabled()) {
            logger.warn(String.format(message, args));
        }
    }

    public void info(String message, Object... args) {
        if (logger.isInfoEnabled()) {
            logger.warn(String.format(message, args));
        }
    }

    public void warn(String message, Object... args) {
        if (logger.isWarnEnabled()) {
            logger.warn(String.format(message, args));
        }
    }

    public void error(String message, Object... args) {
        if (logger.isErrorEnabled()) {
            String formattedMessage = String.format(message, args);
            logger.error(formattedMessage);
        }
    }
}
