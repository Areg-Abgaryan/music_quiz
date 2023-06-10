/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.orchestrators;

import com.areg.project.MusicDatabase;
import com.areg.project.QuizModeContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class OrchestratorBase {

    protected static final Logger logger = LoggerFactory.getLogger(OrchestratorBase.class);
    protected final MusicDatabase musicDatabase = MusicDatabase.getMusicDBInstance();

    /**
     * @param quizModeContext Settings about the concrete quiz mode
     */
    abstract void startQuiz(QuizModeContext quizModeContext);
}