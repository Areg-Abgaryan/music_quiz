/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project;

import com.areg.project.managers.FileParsingManager;
import com.areg.project.managers.QuizWorkflowManager;

public class Main {

    private static final FileParsingManager fileParsingManager = new FileParsingManager();
    private static final String dataFilesPath = fileParsingManager.getDataFilesDirectory();
    private static final QuizWorkflowManager quizWorkflowManager = new QuizWorkflowManager();

    public static void main(String[] args) {
        fileParsingManager.parseDataFiles(dataFilesPath);
        quizWorkflowManager.initQuiz();
    }
}