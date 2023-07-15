/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.QuizConstants;
import com.areg.project.QuizDifficulty;
import com.areg.project.QuizContext;
import com.areg.project.QuizMode;
import com.areg.project.entities.Artist;
import com.areg.project.orchestrators.AlbumToArtistsOrchestrator;
import com.areg.project.orchestrators.ArtistToAlbumsOrchestrator;
import com.areg.project.orchestrators.OrchestratorBase;
import com.areg.project.orchestrators.SongToAlbumsOrchestrator;
import com.areg.project.orchestrators.SongToArtistsOrchestrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Scanner;

//  FIXME !! Init in Autowired constructors all Spring beans
//  FIXME !! Add survival mode support for each submode
//  FIXME !! Add exit & goto beginning logic in the end
//  FIXME !! Parse all the data into the database, remove run-time local db creation
//  FIXME !! Create a log file, save there, & config it with logging.properties
@Service
public class QuizWorkflowManager {

    private static final Logger logger = LoggerFactory.getLogger(QuizWorkflowManager.class);

    private OrchestratorBase orchestratorBase;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public QuizWorkflowManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void initQuiz() {

        System.out.print("Hey ! Welcome to Music Quiz !");

        authenticationManager.authenticate();

        //  FIXME !! After authentication, choose play game or see my info or see my records

        System.out.print("""
                \nModes that are currently supported :
                1. Album from Artists
                2. Artist from Albums
                3. Song from Artists
                4. Song from Albums
                
                Enter quiz mode :\s""");


        //  FIXME !! Consider adding fields from user input, refactor hardcoded everything
        var quizModeContext = new QuizContext();
        quizModeContext.setDifficulty(QuizDifficulty.EASY);
        quizModeContext.setNumberOfRounds(QuizConstants.NumberOfRounds);

        final var scanner = new Scanner(System.in);
        String baseMode = scanner.next();

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
}
