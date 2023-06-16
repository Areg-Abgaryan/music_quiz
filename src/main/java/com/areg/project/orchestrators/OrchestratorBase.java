/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.orchestrators;

import com.areg.project.MusicDatabase;
import com.areg.project.QuizConstants;
import com.areg.project.QuizModeContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public abstract class OrchestratorBase {

    protected static final Logger logger = LoggerFactory.getLogger(OrchestratorBase.class);
    protected final MusicDatabase musicDatabase = MusicDatabase.getMusicDBInstance();

    /**
     * @param quizModeContext Settings about the concrete quiz mode
     */
    abstract void startQuiz(QuizModeContext quizModeContext);

    protected Map<String, Integer> subtypeToOption(Set<String> fourOptions) {
        int i = 1;
        Map<String, Integer> subtypeToOption = new HashMap<>(QuizConstants.RoundOptions);
        for (var artist : fourOptions) {
            System.out.print(i + ". " + artist);
            if (i != 4) {
                System.out.print("  ");
            }
            subtypeToOption.put(artist, i);
            ++i;
        }
        return subtypeToOption;
    }

    //  This method updates the score after each round based on the answer of the user
    protected int answerCheck(Map<String, Integer> subtypeToOption, String correctAnswer, int score) {

        System.out.print("\n\nPlease, choose the correct option : ");

        //  FIXME !! Add timeout logic for each answer, ~30 secs
        while (true) {
            final var scanner = new Scanner(System.in);
            final String option = scanner.next();
            //  FIXME !! Here, optimize 1-1 check
            if (! (Objects.equals(option, "1") || Objects.equals(option, "2") ||
                    Objects.equals(option, "3") || Objects.equals(option, "4"))) {
                System.out.println("Wrong input ! Please choose from the options above.");
            } else {
                final var correctNumber = subtypeToOption.get(correctAnswer);
                if (Integer.parseInt(option) == correctNumber) {
                    ++score;
                    System.out.println("Exactly !");
                } else {
                    System.out.println("No :( ! The correct answer is : " + correctNumber + ". " + correctAnswer);
                }
                break;
            }
        }
        return score;
    }
}