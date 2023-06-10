/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.orchestrators;

import com.areg.project.QuizConstants;
import com.areg.project.QuizModeContext;
import com.areg.project.models.MusicAlbum;
import com.areg.project.models.MusicArtist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AlbumsOrchestrator extends OrchestratorBase {

    @Override
    public void startQuiz(QuizModeContext quizModeContext) {

        //  Giving quizModeContext.numberOfRounds albums,
        //  The user needs to choose the right artist at each step.

        if (quizModeContext.getNumberOfRounds() > musicDatabase.getAlbums().size()) {
            logger.debug("Too many rounds required to play");
            return;
        }

        final Map<MusicAlbum, Boolean> albumsUsedInTheGame = musicDatabase.getAlbumToArtistMap().keySet()
                .stream().collect(Collectors.toMap(Function.identity(), v -> false));
        final List<MusicAlbum> albumsList = new ArrayList<>(albumsUsedInTheGame.keySet());

        final List<MusicArtist> artistsList = new ArrayList<>(musicDatabase.getArtists());

        //   FIXME !! Add a score tracker, then add to the db with the user's info
        int score = 0;
        final int rounds = quizModeContext.getNumberOfRounds();

        //  Building correct & wrong answers candidates
        for (int r = 1; r <= rounds; ++r) {

            MusicAlbum album;
            //  Find an album that was never used for next quiz round
            for ( ; ; ) {
                final MusicAlbum randomKey = albumsList.get(new Random().nextInt(albumsList.size()));
                final Boolean randomValue = albumsUsedInTheGame.get(randomKey);

                //  FIXME !! Try to call randomizer directly from the map, not create list for it
                if (! randomValue) {
                    album = randomKey;
                    albumsUsedInTheGame.put(randomKey, true);
                    break;
                }
            }

            //  Here is the right answer
            final String correctAnswer = musicDatabase.getAlbumToArtistMap().get(album).getName();
            Set<String> fourArtists = new HashSet<>(Collections.singleton(correctAnswer));

            //  Adding 3 wrong answers
            while (fourArtists.size() != 4) {
                fourArtists.add(artistsList.get(new Random().nextInt(artistsList.size())).getName());
            }

            //  Asking user to choose the correct answer
            System.out.println("\nRound " + r + " :\n");
            System.out.println("Album : \"" + album.getName() + "\"");
            System.out.print("Artists : ");

            int i = 1;
            Map<String, Integer> artistToOption = new HashMap<>(QuizConstants.RoundOptions);
            for (var artist : fourArtists) {
                System.out.print(i + ". " + artist);
                if (i != 4) {
                    System.out.print("  ");
                }
                artistToOption.put(artist, i);
                ++i;
            }

            System.out.print("\n\nPlease, choose the correct artist of the album : ");

            while (true) {
                final var scanner = new Scanner(System.in);
                final int option = scanner.nextInt();
                //  FIXME !! Here, check double and other characters input logic
                if (! (option >= 1 && option <= 4)) {
                    System.out.println("Wrong input ! Please choose from the options above.");
                } else {
                    final var correctNumber =  artistToOption.get(correctAnswer);
                    if (option == correctNumber) {
                        ++score;
                        System.out.println("Exactly ! Total points : " + score);
                    } else {
                        System.out.println("No :( ! The correct answer is : " + correctNumber + "." + correctAnswer);
                    }
                    break;
                }
            }
        }
        System.out.println("\nYour final score is : " + score);
    }
}
