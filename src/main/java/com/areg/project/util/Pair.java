/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.util;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Class that contains two objects of generic elements, which may be of different types.
 * Useful for functions that need to return two values.
 *
 * @param <T> type of the first element
 * @param <E> type of the second element
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Pair<T, E> implements Serializable {

    private T first;
    private E second;
}