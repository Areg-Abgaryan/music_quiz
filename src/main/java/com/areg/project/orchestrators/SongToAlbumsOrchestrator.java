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
 *  Giving quizModeContext.numberOfRounds songs,
 *  The user needs to choose the right album at each step.
 */
public class SongToAlbumsOrchestrator extends OrchestratorBase {
    @Override
    public void startQuiz(QuizModeContext quizModeContext) {

        if (quizModeContext.getNumberOfRounds() > musicDatabase.getAlbums().size()) {
            logger.debug("Too many rounds required to play");
            return;
        }

        //  TODO !!

    }
}
