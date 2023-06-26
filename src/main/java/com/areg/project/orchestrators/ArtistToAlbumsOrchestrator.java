/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.orchestrators;

import com.areg.project.QuizModeContext;
import com.areg.project.models.MusicAlbum;
import com.areg.project.models.MusicArtist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *  Giving quizModeContext.numberOfRounds artists,
 *  The user needs to choose the right album that he released at each step.
 */
public class ArtistToAlbumsOrchestrator extends OrchestratorBase {

    @Override
    public void startQuiz(QuizModeContext quizModeContext) {

        if (! isValidQuizModeContext(quizModeContext)) {
            return;
        }

        final Map<MusicArtist, Boolean> artistsUsedInTheGame = musicDatabase.getArtists().stream()
                .collect(Collectors.toMap(album -> album, album -> false, (a, b) -> b));
        final List<MusicArtist> artistsList = new ArrayList<>(artistsUsedInTheGame.keySet());
        final List<MusicAlbum> albumsList = new ArrayList<>(musicDatabase.getAlbums());

        int score = 0;
        final int rounds = quizModeContext.getNumberOfRounds();

        //  Building correct & wrong answers candidates
        for (int r = 1; r <= rounds; ++r) {

            final MusicArtist artist = super.getRandomItem(artistsList, artistsUsedInTheGame);

            //  Here is the right answer
            final List<MusicAlbum> artistAlbumsList = new ArrayList<>(musicDatabase.getArtistToAlbumsMap().get(artist));
            final String correctAnswer = artistAlbumsList.get(new Random().nextInt(artistAlbumsList.size())).getName();
            Set<String> fourAlbums = new HashSet<>(Collections.singleton(correctAnswer));

            //  Adding 3 wrong answers
            while (fourAlbums.size() != 4) {

                //  Get only albums of not right artist's ones
                final int randomAlbumIndex = new Random().nextInt(albumsList.size());
                final var randomAlbum = albumsList.get(randomAlbumIndex);
                if (! randomAlbum.getArtist().equals(correctAnswer)) {

                    boolean repeatingArtistInAlbums = false;
                    for (var a : fourAlbums) {
                        if (randomAlbum.getArtist().equals(a)) {
                            repeatingArtistInAlbums = true;
                            break;
                        }
                    }

                    if (! repeatingArtistInAlbums) {
                        fourAlbums.add(randomAlbum.getName());
                    }
                }
            }

            //  Asking user to choose the correct answer
            System.out.println("\nRound " + r + " :\n");
            System.out.println("Artist : \"" + artist.getName() + "\"");
            System.out.print("Album : ");

            final var artistToOption = super.subtypeToOption(fourAlbums);
            score = super.answerCheck(artistToOption, correctAnswer, score);
        }
        System.out.println("\nYour final score is : " + score);
    }
}
