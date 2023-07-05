/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.QuizConstants;
import com.areg.project.QuizDifficulty;
import com.areg.project.QuizModeContext;
import com.areg.project.orchestrators.AlbumToArtistsOrchestrator;
import com.areg.project.orchestrators.ArtistToAlbumsOrchestrator;
import com.areg.project.orchestrators.OrchestratorBase;
import com.areg.project.orchestrators.SongToAlbumsOrchestrator;
import com.areg.project.orchestrators.SongToArtistsOrchestrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Scanner;

//  FIXME !! Init in Autowired constructors all Spring beans
//  FIXME !! Add survival mode support for each submode
//  FIXME !! Add exit & goto beginning logic in the end
//  FIXME !! Parse all the data into the database, remove run-time local db creation
@Service
public class QuizWorkflowManager {

    private static final Logger logger = LoggerFactory.getLogger(QuizWorkflowManager.class);

    private OrchestratorBase orchestratorBase;

    private AuthenticationManager authenticationManager;

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

        final var scanner = new Scanner(System.in);
        String mode = scanner.next();

        //  FIXME !! Refactor this
        while (true) {
            if (Objects.equals(mode, "1") || Objects.equals(mode, "2")
                    || Objects.equals(mode, "3") || Objects.equals(mode, "4")) {
                logger.debug("Starting quiz mode {}.", mode);
                break;
            }
            System.out.println("Wrong input ! Please, choose one of the above mentioned modes.");
            mode = scanner.next();
        }

        //  FIXME !! Add fields from user input
        var quizModeContext = new QuizModeContext(QuizConstants.NumberOfRounds, QuizDifficulty.EASY);

        //  FIXME !! Add timeout wait logic
        switch (mode) {
            case "1" -> {
                orchestratorBase = new AlbumToArtistsOrchestrator();
                orchestratorBase.startQuiz(quizModeContext);
            }
            case "2" -> {
                orchestratorBase = new ArtistToAlbumsOrchestrator();
                orchestratorBase.startQuiz(quizModeContext);
            }
            case "3" -> {
                orchestratorBase = new SongToArtistsOrchestrator();
                orchestratorBase.startQuiz(quizModeContext);
            }
            case "4" -> {
                orchestratorBase = new SongToAlbumsOrchestrator();
                orchestratorBase.startQuiz(quizModeContext);
            }
        }
    }
}
