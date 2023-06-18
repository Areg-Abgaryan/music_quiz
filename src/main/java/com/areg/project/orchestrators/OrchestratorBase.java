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
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

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
            System.out.print(i + ". \"" + artist + "\"");
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

        System.out.print("\n\nEnter your choice in " + QuizConstants.RoundTimeoutSeconds + "seconds : ");

        final String option = getOptionFromUserInput();
        if (option.equals("")) {
            return score;
        }
        if (! isValidOption(option)) {
            logger.debug("Wrong input : {} ", option);
            System.out.println("Wrong input : \"" + option + "\"");
        } else {
            final var correctNumber = subtypeToOption.get(correctAnswer);
            if (Integer.parseInt(option) == correctNumber) {
                ++score;
                System.out.println("Exactly !");
            } else {
                System.out.println("No :( ! The correct answer is : " + correctNumber + ". " + correctAnswer);
            }
        }
        return score;
    }

    private String getOptionFromUserInput() {

        final ExecutorService service = Executors.newFixedThreadPool(1);
        final Callable<String> callable = () -> new Scanner(System.in).next();
        final Future<String> inputFuture = service.submit(callable);

        try {
            return inputFuture.get(QuizConstants.RoundTimeoutSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException("Thread was interrupted", e);
        } catch (TimeoutException e) {
            // Tell user they timed out
            System.out.println("\nTime out !");
        } finally {
            service.shutdown();
        }
        return "";
    }

    private boolean isValidOption(String option) {
        if (option == null) {
            return false;
        }
        try {
            int intOption = Integer.parseInt(option);
            return IntStream.rangeClosed(1, QuizConstants.RoundOptions).anyMatch(i -> i == intOption);
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

}