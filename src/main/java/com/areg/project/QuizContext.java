/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuizContext {
    private QuizMode mode;
    private QuizDifficulty difficulty;
    private int numberOfRounds;
}
