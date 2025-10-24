package com.example.sorting_algorithm_visualiser;

import java.util.List;

/**
 *guarantees that every algorithm will have a 'sort' method that
 * takes an array and returns a list of animation actions.
 */
public interface SortingStrategy {
    /**
     * sorts a copy of the given array and returns a list of actions
     * representing the steps taken.
     *
     * @param array The array to be sorted.
     * @return list of SortAction steps for the animator.
     */
    List<SortAction> sort(int[] array);
}
