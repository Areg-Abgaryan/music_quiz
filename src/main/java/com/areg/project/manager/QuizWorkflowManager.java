/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.manager;

import com.areg.project.QuizContext;
import com.areg.project.QuizMode;
import com.areg.project.orchestrator.AlbumToArtistsOrchestrator;
import com.areg.project.orchestrator.ArtistToAlbumsOrchestrator;
import com.areg.project.orchestrator.OrchestratorBase;
import com.areg.project.orchestrator.SongToAlbumsOrchestrator;
import com.areg.project.orchestrator.SongToArtistsOrchestrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Scanner;

//  FIXME !! Add more logs everywhere
//  FIXME !! Write script for db migration
//  FIXME !! Try to run into Docker container
//  FIXME !! Add a pipeline for the project
//  FIXME !! Add caching mechanism
//  FIXME !! Add functional, integration, end-to-end tests
//  FIXME !! Add Difficulty logic in orchestrators;
//  FIXME !! Add a score tracker, then add to the db with the user's info
//  FIXME !! Add survival mode support for each submode
//  FIXME !! Add exit & goto beginning logic in the end
//  FIXME !! Add unit tests coverage throughout the project
//  FIXME !! Make microservice from authentication logic
//  FIXME !! After authentication, choose play game or see my info or see my records
//  FIXME !! Consider authenticating in a loop, sign out, sign in maybe
//  FIXME !! Add Roles & Permissions for users then
//  FIXME !! Remove run-time database creation & get all information from the persistent db

@Service
public class QuizWorkflowManager {

    private static final Logger logger = LoggerFactory.getLogger(QuizWorkflowManager.class);
    private OrchestratorBase orchestratorBase;

    public void initQuiz() {
        printAvailableModes();

        // Get quiz mode context from user input
        final QuizContext quizContext = getQuizModeContextFromUserInput();

        // Start the quiz based on the selected mode
        orchestratorBase.startQuiz(quizContext);
    }

    private QuizContext getQuizModeContextFromUserInput() {

        final var scanner = new Scanner(System.in);

        while (true) {
            final String input = scanner.next();
            //  FIXME !! Add timeout wait logic
            switch (input) {
                case "1" -> {
                    return initializeQuizMode(QuizMode.Normal_Album_from_Artists, new AlbumToArtistsOrchestrator());
                }
                case "2" -> {
                    return initializeQuizMode(QuizMode.Normal_Artist_from_Albums, new ArtistToAlbumsOrchestrator());
                }
                case "3" -> {
                    return initializeQuizMode(QuizMode.Normal_Song_from_Artists, new SongToArtistsOrchestrator());
                }
                case "4" -> {
                    return initializeQuizMode(QuizMode.Normal_Artist_from_Albums, new SongToAlbumsOrchestrator());
                }
                default -> System.out.println("Wrong input! Please, choose one of the options above.");
            }
        }
    }

    private QuizContext initializeQuizMode(QuizMode mode, OrchestratorBase orchestrator) {
        //  FIXME !! Consider adding fields from user input, refactor hardcoded everything
        final var quizContext = QuizContext.getDefaultContext();
        quizContext.setMode(mode);
        orchestratorBase = orchestrator;

        logger.info("Quiz difficulty : {}", quizContext.getDifficulty());
        logger.info("Quiz number of rounds : {}", quizContext.getNumberOfRounds());
        logger.info("Music input mode : {}", mode);

        return quizContext;
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
