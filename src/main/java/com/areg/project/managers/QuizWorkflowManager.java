/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.QuizContext;
import com.areg.project.QuizMode;
import com.areg.project.orchestrators.AlbumToArtistsOrchestrator;
import com.areg.project.orchestrators.ArtistToAlbumsOrchestrator;
import com.areg.project.orchestrators.OrchestratorBase;
import com.areg.project.orchestrators.SongToAlbumsOrchestrator;
import com.areg.project.orchestrators.SongToArtistsOrchestrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Scanner;

//  FIXME !! Add Difficulty logic in orchestrators;
//  FIXME !! Add a score tracker, then add to the db with the user's info
//  FIXME !! Add survival mode support for each submode
//  FIXME !! Add exit & goto beginning logic in the end
//  FIXME !! Replace Hibernate#save with #persist
//  FIXME !! Add logic for recovering forgotten password
//  FIXME !! Make microservice from authentication logic
//  FIXME !! After authentication, choose play game or see my info or see my records
//  FIXME !! Consider authenticating in a loop, sign out, sign in maybe
//  FIXME !! When the user can't authenticate successfully, he still can start the quiz
//  FIXME !! Add Roles & Permissions for users then
//  FIXME !! Remove run-time database creation & get all information from the persistent db
@Service
public class QuizWorkflowManager {

    private static final Logger logger = LoggerFactory.getLogger(QuizWorkflowManager.class);
    private OrchestratorBase orchestratorBase;

    public void initQuiz() {

        printAvailableModes();

        //  FIXME !! Consider adding fields from user input, refactor hardcoded everything
        final var quizModeContext = QuizContext.getDefaultContext();
        logger.info("Quiz difficulty : {}", quizModeContext.getDifficulty());
        logger.info("Quiz number of rounds : {}", quizModeContext.getNumberOfRounds());

        final var scanner = new Scanner(System.in);
        String baseMode = scanner.next();
        logger.info("Music input mode : {}", baseMode);

        //  FIXME !! Add timeout wait logic
        //  FIXME !! Refactor this
        boolean isInputValid = false;
        do {
            switch (baseMode) {
                case "1" -> {
                    isInputValid = true;
                    quizModeContext.setMode(QuizMode.Normal_Album_from_Artists);
                    orchestratorBase = new AlbumToArtistsOrchestrator();
                }
                case "2" -> {
                    isInputValid = true;
                    quizModeContext.setMode(QuizMode.Normal_Artist_from_Albums);
                    orchestratorBase = new ArtistToAlbumsOrchestrator();
                }
                case "3" -> {
                    isInputValid = true;
                    quizModeContext.setMode(QuizMode.Normal_Song_from_Artists);
                    orchestratorBase = new SongToArtistsOrchestrator();
                }
                case "4" -> {
                    isInputValid = true;
                    quizModeContext.setMode(QuizMode.Normal_Artist_from_Albums);
                    orchestratorBase = new SongToAlbumsOrchestrator();
                }
                default -> {
                    System.out.println("Wrong input ! Please, choose one of the options above.");
                    baseMode = scanner.next();
                }
            }
        } while (! isInputValid);

        orchestratorBase.startQuiz(quizModeContext);
    }

    private void printAvailableModes() {
        System.out.print("""
                \nModes that are currently supported :
                1. Album from Artists
                2. Artist from Albums
                3. Song from Artists
                4. Song from Albums
                
                Enter quiz mode :\s""");
    }
}
