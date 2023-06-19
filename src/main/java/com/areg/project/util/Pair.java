/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

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
public class Pair<T, E> implements Serializable {

    private T first;
    private E second;

    @Override
    public String toString() {
        return "<" + this.first + "; " + this.second + ">";
    }

    @Override
    public boolean equals(final Object second) {
        if (! (second instanceof Pair other)) {
            return false;
        }
        return Objects.equals(this.first, other.first) && Objects.equals(this.second, other.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.first, this.second);
    }
}