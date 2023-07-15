/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project;

import com.areg.project.managers.ArtistManager;
import com.areg.project.managers.AuthenticationManager;
import com.areg.project.managers.FileParsingManager;
import com.areg.project.managers.QuizWorkflowManager;
import com.areg.project.managers.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

//  FIXME !! Refactor everything

/**
 * Music Quiz Java application
 * @author Areg Abgaryan
 */
@SpringBootApplication
@EntityScan
public class QuizApplication {

    //  FIXME !! Refactor this
    private static final FileParsingManager fileParsingManager = new FileParsingManager();
    private static final String dataFilesPath = fileParsingManager.getDataFilesDirectory();
    private static final UserManager userManager = new UserManager();
    private static final ArtistManager artistManager = new ArtistManager();
    private static final AuthenticationManager authenticationManager = new AuthenticationManager(userManager, artistManager);
    private static final QuizWorkflowManager quizWorkflowManager = new QuizWorkflowManager(authenticationManager);

    public static void main(String[] args) {
        SpringApplication.run(QuizApplication.class, args);
        fileParsingManager.parseDataFiles(dataFilesPath);
        quizWorkflowManager.initQuiz();
    }
}