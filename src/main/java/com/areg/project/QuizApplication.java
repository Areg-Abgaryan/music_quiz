/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project;

import com.areg.project.managers.AuthenticationManager;
import com.areg.project.managers.FileParsingManager;
import com.areg.project.managers.QuizWorkflowManager;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * Music Quiz Application
 *
 * @since 2023
 * @version 1.0
 * @author Areg Abgaryan
 */
//  FIXME !! After authentication, choose play game or see my info or see my records
//  FIXME !! Consider authenticating in a loop, sign out, sign in maybe
@SpringBootApplication
@EntityScan
public class QuizApplication {

    private final FileParsingManager fileParsingManager;
    private final AuthenticationManager authenticationManager;
    private final QuizWorkflowManager quizWorkflowManager;

    @Autowired
    public QuizApplication(FileParsingManager fileParsingManager, AuthenticationManager authenticationManager,
                           QuizWorkflowManager quizWorkflowManager) {
        this.fileParsingManager = fileParsingManager;
        this.authenticationManager = authenticationManager;
        this.quizWorkflowManager = quizWorkflowManager;
    }

    public static void main(String[] args) {
        SpringApplication.run(QuizApplication.class, args);
    }

    @PostConstruct
    public void initQuiz() {
        final String dataFilesPath = fileParsingManager.getDataFilesDirectory();
        fileParsingManager.parseDataFiles(dataFilesPath);
        authenticationManager.authenticate();
        quizWorkflowManager.initQuiz();
    }
}
