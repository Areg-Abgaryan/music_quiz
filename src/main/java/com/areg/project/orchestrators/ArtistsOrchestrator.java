/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.orchestrators;

import com.areg.project.QuizModeContext;

public class ArtistsOrchestrator extends OrchestratorBase {

    @Override
    public void startQuiz(QuizModeContext quizModeContext) {
        //  Giving quizModeContext.numberOfRounds artists, the user needs to choose the right album that he created.
        System.out.println("Unfortunately, currently this mode is not supported :(");
    }
}
