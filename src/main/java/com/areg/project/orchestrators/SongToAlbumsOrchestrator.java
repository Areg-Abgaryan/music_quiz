/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.orchestrators;

import com.areg.project.QuizContext;
import com.areg.project.models.MusicAlbum;
import com.areg.project.models.MusicSong;
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
 *  Giving quizModeContext.numberOfRounds songs,
 *  The user needs to choose the right album at each step.
 */
@Service
public class SongToAlbumsOrchestrator extends OrchestratorBase {

    @Override
    public void startQuiz(QuizContext quizContext) {

        if (! isValidQuizModeContext(quizContext)) {
            return;
        }

        super.startQuiz(quizContext);

        final Map<MusicSong, Boolean> songsUsedInTheGame = musicDatabase.getSongs().stream()
                .collect(Collectors.toMap(album -> album, album -> false, (a, b) -> b));
        final List<MusicAlbum> albumsList = new ArrayList<>(musicDatabase.getAlbums());

        short score = 0;
        final short rounds = quizContext.getNumberOfRounds();

        //  Building correct & wrong answers candidates
        for (short r = 1; r <= rounds; ++r) {

            final MusicSong song = super.getRandomItem(new ArrayList<>(songsUsedInTheGame.keySet()), songsUsedInTheGame);

            //  Here is the right answer
            final String correctAnswer = song.getAlbum().getName();
            final Set<String> fourAlbums = new HashSet<>(Collections.singleton(correctAnswer));

            //  Adding 3 wrong answers
            while (fourAlbums.size() != 4) {
                fourAlbums.add(albumsList.get(new Random().nextInt(albumsList.size())).getName());
            }

            //  Asking user to choose the correct answer
            System.out.println("\nRound " + r + " :\n");
            System.out.println("Song : \"" + song.getName() + "\"");
            System.out.print("Albums : ");

            final var artistToOption = super.subtypeToOption(fourAlbums);
            score = super.answerCheck(artistToOption, correctAnswer, score);
        }
        System.out.println("\nYour final score is : " + score);
    }
}
