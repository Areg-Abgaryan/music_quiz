/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizContext {

    private QuizMode mode;
    private QuizDifficulty difficulty;
    private short numberOfRounds;

    public static QuizContext getDefaultContext() {
        final var context = new QuizContext();
        context.setDifficulty(QuizDifficulty.EASY);
        context.setNumberOfRounds(QuizConstants.NumberOfRounds);
        return context;
    }
}
