/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.QuizDifficulty;
import com.areg.project.QuizModeContext;
import com.areg.project.orchestrators.AlbumsOrchestrator;
import com.areg.project.orchestrators.ArtistsOrchestrator;
import com.areg.project.orchestrators.SongsOrchestrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Scanner;

public class QuizWorkflowManager {

    private static final Logger logger = LoggerFactory.getLogger(QuizWorkflowManager.class);

    //  FIXME !! Make these managers as Spring.io Services, & init in Autowired constructors
    private final AlbumsOrchestrator albumsOrchestrator = new AlbumsOrchestrator();
    private final SongsOrchestrator songsOrchestrator = new SongsOrchestrator();
    private final ArtistsOrchestrator artistsOrchestrator = new ArtistsOrchestrator();

    public void initQuiz() {

        //  FIXME !! Add survival mode support for each submode
        //  FIXME !! Ask for user's name to track records for each mode, here is when the need for the db comes
        System.out.print("""
                Hey ! Welcome to Music Quiz !
                
                Modes that are currently supported :
                1. Albums 2. Songs 3. Artists
                
                Enter quiz mode :\s""");

        final var scanner = new Scanner(System.in);
        String mode = scanner.next();

        while (true) {
            if (Objects.equals(mode, "1") || Objects.equals(mode, "2") || Objects.equals(mode, "3")) {
                logger.debug("Starting quiz mode {}", mode);
                break;
            }
            System.out.println("Wrong input ! Please, choose one of the above mentioned modes.");
            mode = scanner.next();
        }

        //  FIXME !! Add fields from user input
        var quizModeContext = new QuizModeContext(5, QuizDifficulty.EASY);

        //  FIXME !! Add timeout wait logic
        switch (mode) {
            case "1" ->
                    albumsOrchestrator.startQuiz(quizModeContext);
            case "2" ->
                    songsOrchestrator.startQuiz(quizModeContext);
            case "3" ->
                    artistsOrchestrator.startQuiz(quizModeContext);
        }
    }
}
