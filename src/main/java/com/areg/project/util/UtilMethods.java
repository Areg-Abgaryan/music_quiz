/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UtilMethods {

    public static boolean isOptionInValidRange(String option, int lowerBound, int upperBound) {

        if (option == null || option.isEmpty()) {
            return false;
        }

        try {
            int intOption = Integer.parseInt(option);
            return IntStream.rangeClosed(lowerBound, upperBound).anyMatch(i -> i == intOption);
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
