/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.QuizConstants;
import com.areg.project.QuizDifficulty;
import com.areg.project.QuizModeContext;
import com.areg.project.entities.User;
import com.areg.project.orchestrators.AlbumToArtistsOrchestrator;
import com.areg.project.orchestrators.ArtistToAlbumsOrchestrator;
import com.areg.project.orchestrators.OrchestratorBase;
import com.areg.project.orchestrators.SongToAlbumsOrchestrator;
import com.areg.project.orchestrators.SongToArtistsOrchestrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Scanner;

@Service
public class QuizWorkflowManager {

    private static final Logger logger = LoggerFactory.getLogger(QuizWorkflowManager.class);

    //  FIXME !! Keep only one base class instance and initialize as an argument child class
    //  FIXME !! Make these managers as Spring.io Services, & init in Autowired constructors

    private OrchestratorBase orchestratorBase;

    //  @Autowired
    //  public QuizWorkflowManager(AlbumToArtistsOrchestrator albumToArtistsOrchestrator,
    //                             ArtistToAlbumsOrchestrator artistToAlbumsOrchestrator,
    //                             SongToArtistsOrchestrator songToArtistsOrchestrator,
    //                             SongToAlbumsOrchestrator songToAlbumsOrchestrator) {
    //      this.albumToArtistsOrchestrator = albumToArtistsOrchestrator;
    //      this.artistToAlbumsOrchestrator = artistToAlbumsOrchestrator;
    //      this.songToArtistsOrchestrator = songToArtistsOrchestrator;
    //      this.songToAlbumsOrchestrator = songToAlbumsOrchestrator;
    //  }
    public void initQuiz() {

        //  FIXME !! Add survival mode support for each submode
        //  FIXME !! Ask for user's name to track records for each mode, here is when the need for the db comes

        UserManager.createUser(new User("ss", "@mail", "1234"));


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
