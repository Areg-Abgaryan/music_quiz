/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project;

import com.areg.project.managers.FileParsingManager;
import com.areg.project.managers.QuizWorkflowManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Music Quiz Java application
 * @author Areg Abgaryan
 */
@SpringBootApplication
public class QuizApplication {

    private static final FileParsingManager fileParsingManager = new FileParsingManager();
    private static final String dataFilesPath = fileParsingManager.getDataFilesDirectory();
    private static final QuizWorkflowManager quizWorkflowManager = new QuizWorkflowManager();


    //  FIXME !! Refactor everything
    //  FIXME !! Check 2nd mode : F. ex. : ACDC : Highway to Hell , Back in Black
    //  FIXME !! Check when timeout is 20 seconds, it doesn't work
    public static void main(String[] args) {
        SpringApplication.run(QuizApplication.class, args);
        fileParsingManager.parseDataFiles(dataFilesPath);
        quizWorkflowManager.initQuiz();
    }
}