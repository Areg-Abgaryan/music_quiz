/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.orchestrators;

import com.areg.project.QuizModeContext;
import com.areg.project.models.MusicAlbum;
import com.areg.project.models.MusicArtist;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  Giving quizModeContext.numberOfRounds artists,
 *  The user needs to choose the right album that he released at each step.
 */
public class ArtistsOrchestrator extends OrchestratorBase {

    @Override
    public void startQuiz(QuizModeContext quizModeContext) {

        if (quizModeContext.getNumberOfRounds() > musicDatabase.getAlbums().size()) {
            logger.debug("Too many rounds required to play");
            return;
        }

        //  We need here all artists
        //  We need here all albums
        final Map<MusicAlbum, Boolean> albumsUsedInTheGame = musicDatabase.getAlbums().stream()
                .collect(Collectors.toMap(album -> album, album -> false, (a, b) -> b));

        final List<MusicArtist> artistsList = new ArrayList<>(musicDatabase.getArtists());

        //   FIXME !! Add a score tracker, then add to the db with the user's info
        int score = 0;
        final int rounds = quizModeContext.getNumberOfRounds();

        //  1 artist, many albums, it is Map<Artist, Set<Albums>>

        //  Building correct & wrong answers candidates
        for (int r = 1; r <= rounds; ++r) {

        }
    }
}
