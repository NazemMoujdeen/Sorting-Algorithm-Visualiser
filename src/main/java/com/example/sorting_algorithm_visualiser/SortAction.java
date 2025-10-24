package com.example.sorting_algorithm_visualiser;

/**
 *
 * sealed interface ensures that a SortAction can ONLY be one of the
 * types defined in this file (SwapAction or OverwriteAction so far).
 */
public sealed interface SortAction permits SwapAction, OverwriteAction {
}

/**
 * represents a swap between two indices. (used in only bubble sort so  far)
 */
final class SwapAction implements SortAction {
    public final int index1;
    public final int index2;

    public SwapAction(int index1, int index2) {
        this.index1 = index1;
        this.index2 = index2;
    }
}

/**
 * represents overwriting an index with a new value. (used in merge sort)
 */
final class OverwriteAction implements SortAction {
    public final int index;
    public final int value;

    public OverwriteAction(int index, int value) {
        this.index = index;
        this.value = value;
    }
}

