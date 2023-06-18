/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.QuizDifficulty;
import com.areg.project.QuizModeContext;
import com.areg.project.orchestrators.AlbumToArtistsOrchestrator;
import com.areg.project.orchestrators.ArtistToAlbumsOrchestrator;
import com.areg.project.orchestrators.SongToAlbumsOrchestrator;
import com.areg.project.orchestrators.SongToArtistsOrchestrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Scanner;

public class QuizWorkflowManager {

    private static final Logger logger = LoggerFactory.getLogger(QuizWorkflowManager.class);

    //  FIXME !! Make these managers as Spring.io Services, & init in Autowired constructors
    private final AlbumToArtistsOrchestrator albumToArtistsOrchestrator = new AlbumToArtistsOrchestrator();
    private final ArtistToAlbumsOrchestrator artistToAlbumsOrchestrator = new ArtistToAlbumsOrchestrator();
    private final SongToArtistsOrchestrator songToArtistsOrchestrator = new SongToArtistsOrchestrator();
    private final SongToAlbumsOrchestrator songToAlbumsOrchestrator = new SongToAlbumsOrchestrator();

    public void initQuiz() {

        //  FIXME !! Add survival mode support for each submode
        //  FIXME !! Ask for user's name to track records for each mode, here is when the need for the db comes
        System.out.print("""
                Hey ! Welcome to Music Quiz !
                
                Modes that are currently supported :
                1. Album from Artists
                2. Artist from Albums
                3. Song from Artists
                4. Song from Albums
                
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
            case "1" -> albumToArtistsOrchestrator.startQuiz(quizModeContext);
            case "2" -> artistToAlbumsOrchestrator.startQuiz(quizModeContext);
            case "3" -> songToArtistsOrchestrator.startQuiz(quizModeContext);
            case "4" -> songToAlbumsOrchestrator.startQuiz(quizModeContext);
        }
    }
}
