/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.orchestrators;

import com.areg.project.QuizContext;
import com.areg.project.models.MusicAlbum;
import com.areg.project.models.MusicArtist;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *  Giving quizModeContext.numberOfRounds albums,
 *  The user needs to choose the right artist at each step.
 */
@Service
public class AlbumToArtistsOrchestrator extends OrchestratorBase {

    @Override
    public void startQuiz(QuizContext quizContext) {

        if (! isQuizModeContextValid(quizContext)) {
            return;
        }

        super.startQuiz(quizContext);

        final Map<MusicAlbum, Boolean> albumsUsedInTheGame = musicDatabase.getAlbums().stream()
                .collect(Collectors.toMap(album -> album, album -> false, (a, b) -> b));
        final List<MusicArtist> artistsList = new ArrayList<>(musicDatabase.getArtists());

        short score = 0;
        final short rounds = quizContext.getNumberOfRounds();

        //  Building correct & wrong answers candidates
        for (short r = 1; r <= rounds; ++r) {

            final MusicAlbum album = super.getRandomItem(new ArrayList<>(albumsUsedInTheGame.keySet()), albumsUsedInTheGame);

            //  Here is the right answer
            final String correctAnswer = album.getArtist();
            final Set<String> fourArtists = new HashSet<>(Collections.singleton(correctAnswer));

            //  Adding 3 wrong answers
            while (fourArtists.size() != 4) {
                fourArtists.add(artistsList.get(new Random().nextInt(artistsList.size())).getName());
            }

            //  Asking user to choose the correct answer
            System.out.println("\nRound " + r + " :\n");
            System.out.println("Album : \"" + album.getName() + "\"");
            System.out.print("Artists : ");

            final var artistToOption = super.subtypeToOption(fourArtists);
            score = super.answerCheck(artistToOption, correctAnswer, score);
        }
        System.out.println("\nYour final score is : " + score);
    }
}
