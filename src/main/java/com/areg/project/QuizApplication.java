/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project;

import com.areg.project.managers.AuthenticationManager;
import com.areg.project.managers.FileParsingManager;
import com.areg.project.managers.QuizWorkflowManager;
import com.areg.project.managers.UserManager;
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
@SpringBootApplication
@EntityScan
public class QuizApplication {
    //  FIXME !! Init in Autowired constructors all Spring beans
    private static final FileParsingManager fileParsingManager = new FileParsingManager();
    private static final String dataFilesPath = fileParsingManager.getDataFilesDirectory();
    private static final UserManager userManager = new UserManager();
    private static final AuthenticationManager authenticationManager = new AuthenticationManager(userManager);
    private static final QuizWorkflowManager quizWorkflowManager = new QuizWorkflowManager(authenticationManager);

    public static void main(String[] args) {
        SpringApplication.run(QuizApplication.class, args);
        fileParsingManager.parseDataFiles(dataFilesPath);
        //  FIXME !! After authentication, choose play game or see my info or see my records
        //  FIXME !! Consider authenticating in a loop, sign out, sign in maybe
        authenticationManager.authenticate();
        quizWorkflowManager.initQuiz();
    }
}