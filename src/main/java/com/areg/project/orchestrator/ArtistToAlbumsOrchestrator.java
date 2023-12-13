/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.orchestrator;

import com.areg.project.QuizContext;
import com.areg.project.model.MusicAlbum;
import com.areg.project.model.MusicArtist;
import org.springframework.stereotype.Component;

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
@Component
public class ArtistToAlbumsOrchestrator extends OrchestratorBase {

    @Override
    public void startQuiz(QuizContext quizContext) {

        if (! isQuizModeContextValid(quizContext)) {
            return;
        }

        super.startQuiz(quizContext);

        final Map<MusicArtist, Boolean> artistsUsedInTheGame = musicDatabase.getArtists().stream()
                .collect(Collectors.toMap(album -> album, album -> false, (a, b) -> b));
        final List<MusicAlbum> albumsList = new ArrayList<>(musicDatabase.getAlbums());

        short score = 0;
        final short rounds = quizContext.getNumberOfRounds();

        //  Building correct & wrong answers candidates
        for (short r = 1; r <= rounds; ++r) {

            final MusicArtist artist = super.getRandomItem(new ArrayList<>(artistsUsedInTheGame.keySet()), artistsUsedInTheGame);

            //  Here is the right answer
            final List<MusicAlbum> artistAlbumsList = new ArrayList<>(musicDatabase.getArtistToAlbumsMap().get(artist));
            final MusicAlbum correctAlbum = artistAlbumsList.get(new Random().nextInt(artistAlbumsList.size()));
            final Set<String> fourAlbums = new HashSet<>(Collections.singleton(correctAlbum.getName()));

            //  Adding 3 wrong answers
            while (fourAlbums.size() != 4) {

                //  Get only albums of not right artist's ones
                final short randomAlbumIndex = (short) new Random().nextInt(albumsList.size());
                final var randomAlbum = albumsList.get(randomAlbumIndex);
                if (! randomAlbum.getArtist().equals(correctAlbum.getArtist())) {
                    fourAlbums.add(randomAlbum.getName());
                }
            }

            //  Asking user to choose the correct answer
            System.out.println("\nRound " + r + " :\n");
            System.out.println("ArtistEntity : \"" + artist.getName() + "\"");
            System.out.print("AlbumEntity : ");

            final var artistToOption = super.subtypeToOption(fourAlbums);
            score = super.answerCheck(artistToOption, correctAlbum.getName(), score);
        }
        System.out.println("\nYour final score is : " + score);
    }
}
