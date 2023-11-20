/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.QuizConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
//  TODO with invocation handler
public class InactivityMonitoringManager {

    /*private static final Logger logger = LoggerFactory.getLogger(InactivityMonitoringManager.class);
    private Timer inactivityTimer = new Timer();

    public void startMonitoringInactivity() {
        final Console console = System.console();
        //  if (console == null) {
        //      System.err.println("No console is available. Exiting the game");
        //      System.exit(1);
        //  }
        startInputMonitoringThread();
        startInactivityTimer();
    }

    private void startInputMonitoringThread() {
        final var inputMonitoringThread = new Thread(() -> {
            try (var reader = new BufferedReader(new InputStreamReader(System.in))) {
                String userInput;
                while ((userInput = reader.readLine()) != null) {
                    // Monitor user input
                    resetInactivityTimer();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        inputMonitoringThread.start();
    }

    private void startInactivityTimer() {
        inactivityTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Inactivity timeout reached, perform shutdown here
                logger.info("Inactivity timeout reached. Quitting the game.");
                System.out.println("Inactivity timeout reached. Quitting the game.");
                System.exit(1);
            }
        }, QuizConstants.InactivityTimeoutMs);
    }

    private void resetInactivityTimer() {
        inactivityTimer.cancel();
        inactivityTimer = new Timer();
        startInactivityTimer();
    }*/

}
