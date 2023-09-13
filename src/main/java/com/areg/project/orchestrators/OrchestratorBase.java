/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.orchestrators;

import com.areg.project.MusicDatabase;
import com.areg.project.QuizConstants;
import com.areg.project.QuizContext;
import com.areg.project.utils.UtilMethods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public abstract class OrchestratorBase {

    protected static final Logger logger = LoggerFactory.getLogger(OrchestratorBase.class);
    protected final MusicDatabase musicDatabase = MusicDatabase.getMusicDBInstance();

    /**
     * @param quizContext Settings about the concrete quiz mode
     */
    public void startQuiz(QuizContext quizContext) {
        logger.info("Starting quiz mode : {}.", quizContext.getMode());
    }

    protected <Type> Type getRandomItem(List<Type> itemsList, Map<Type, Boolean> itemsUsedInTheGame) {

        //  Find an item that was never used for next quiz round
        Type type;
        for ( ; ; ) {
            final Type randomKey = itemsList.get(new Random().nextInt(itemsList.size()));
            final Boolean randomValue = itemsUsedInTheGame.get(randomKey);
            if (! randomValue) {
                type = randomKey;
                itemsUsedInTheGame.put(randomKey, true);
                break;
            }
        }
        return type;
    }

    protected boolean isQuizModeContextValid(QuizContext quizContext) {

        if (quizContext == null) {
            logger.info("Quiz mode context is null.");
            return false;
        }

        if (quizContext.getNumberOfRounds() > musicDatabase.getAlbums().size()) {
            logger.info("Too many rounds required to play.");
            return false;
        }
        return true;
    }

    protected Map<String, Byte> subtypeToOption(Set<String> fourOptions) {

        byte i = 1;
        final Map<String, Byte> subtypeToOption = new HashMap<>(QuizConstants.RoundOptions);
        for (String option : fourOptions) {
            System.out.print(i + ". \"" + option + "\"" + (i != 4 ? "  " : ""));
            subtypeToOption.put(option, i);
            ++i;
        }
        return subtypeToOption;
    }

    /**
     * This method returns a pair of the score & last answer after each round
     */
    protected short answerCheck(Map<String, Byte> subtypeToOption, String correctAnswer, short score) {

        System.out.print("\n\nEnter your choice in " + QuizConstants.RoundTimeoutSeconds + " seconds : ");

        final String option = getOptionFromUserInput();
        if (option.equals("")) {
            return score;
        }

        if (! UtilMethods.isOptionInValidRange(option, 1, QuizConstants.RoundOptions)) {
            logger.info("Wrong input : {}.", option);
            System.out.println("Wrong input : \"" + option + "\"");
        } else {
            final byte correctNumber = subtypeToOption.get(correctAnswer);
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
        final Callable<String> callable = () -> new Scanner(System.in).nextLine();
        final Future<String> inputFuture = service.submit(callable);

        try {
            return inputFuture.get(QuizConstants.RoundTimeoutSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException("Thread was interrupted", e);
        } catch (TimeoutException e) {
            // Tell user they timed out
            logger.info("Error : Time out !");
            System.out.println("\nTime out !");
        } finally {
            service.shutdown();
        }
        return "";
    }
}