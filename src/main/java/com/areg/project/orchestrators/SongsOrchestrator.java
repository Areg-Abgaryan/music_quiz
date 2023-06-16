/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.orchestrators;

import com.areg.project.QuizModeContext;
import com.areg.project.models.MusicArtist;
import com.areg.project.models.MusicSong;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *  Giving quizModeContext.numberOfRounds songs,
 *  The user needs to choose the right artist at each step.
 */
public class SongsOrchestrator extends OrchestratorBase {

    @Override
    public void startQuiz(QuizModeContext quizModeContext) {

        if (quizModeContext.getNumberOfRounds() > musicDatabase.getAlbums().size()) {
            logger.debug("Too many rounds required to play");
            return;
        }

        final Map<MusicSong, Boolean> songsUsedInTheGame = musicDatabase.getSongs().stream()
                .collect(Collectors.toMap(album -> album, album -> false, (a, b) -> b));
        final List<MusicSong> songsList = new ArrayList<>(songsUsedInTheGame.keySet());
        final List<MusicArtist> artistsList = new ArrayList<>(musicDatabase.getArtists());

        int score = 0;
        final int rounds = quizModeContext.getNumberOfRounds();

        //  Building correct & wrong answers candidates
        for (int r = 1; r <= rounds; ++r) {

            MusicSong song;
            //  Find a song that was never used for next quiz round
            for ( ; ; ) {
                final MusicSong randomKey = songsList.get(new Random().nextInt(songsList.size()));
                final Boolean randomValue = songsUsedInTheGame.get(randomKey);

                //  FIXME !! Try to call randomizer directly from the map, not create list for it
                if (! randomValue) {
                    song = randomKey;
                    songsUsedInTheGame.put(randomKey, true);
                    break;
                }
            }

            //  Here is the right answer
            final String correctAnswer = musicDatabase.getSongToArtistMap().get(song).getName();
            Set<String> fourArtists = new HashSet<>(Collections.singleton(correctAnswer));

            //  Adding 3 wrong answers
            while (fourArtists.size() != 4) {
                fourArtists.add(artistsList.get(new Random().nextInt(artistsList.size())).getName());
            }

            //  Asking user to choose the correct answer
            System.out.println("\nRound " + r + " :\n");
            System.out.println("Song : \"" + song.getName() + "\"");
            System.out.print("Artists : ");

            final var artistToOption = super.subtypeToOption(fourArtists);
            score = super.answerCheck(artistToOption, correctAnswer, score);
        }
        System.out.println("\nYour final score is : " + score);
    }
}
