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
}
