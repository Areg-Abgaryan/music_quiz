/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.orchestrators;

import com.areg.project.QuizModeContext;

public class SongsOrchestrator extends OrchestratorBase {

    @Override
    public void startQuiz(QuizModeContext quizModeContext) {
        //  Giving quizModeContext.numberOfRounds songs, the user needs to choose the right artist.
        System.out.println("Unfortunately, currently this mode is not supported :(");
    }
}
