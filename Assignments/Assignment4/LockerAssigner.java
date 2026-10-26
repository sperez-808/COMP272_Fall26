import java.util.TreeMap;
import java.util.TreeSet;

/**
 * COMP 272 Assignment 4 — Exercise 3: nearest free locker.
 *
 * The library has n lockers numbered 1..n, all free at the start.
 * A student asks for a preferred number and receives the FREE locker closest
 * to it. If two free lockers are equally close, the SMALLER number wins.
 *
 * Use java.util.TreeSet and/or java.util.TreeMap (your choice).
 * Do not change the method signatures. You may add private fields and helpers.
 */
public class LockerAssigner {

    // TODO: declare your field(s) here.

    /** Create lockers 1..n, all free. Throw IllegalArgumentException if n < 0. */
    public LockerAssigner(int n) {
        // TODO
    }

    /**
     * Assign the free locker closest to preferred (ties: smaller number),
     * mark it as taken, and return its number. preferred may be any int,
     * even outside 1..n. Return -1 if no locker is free.
     */
    public int assign(int preferred) {
        throw new UnsupportedOperationException("TODO: LockerAssigner.assign");
    }

    /**
     * Make locker free again and return true. Return false (and change
     * nothing) if it is already free or is not a number in 1..n.
     */
    public boolean release(int locker) {
        throw new UnsupportedOperationException("TODO: LockerAssigner.release");
    }

    /*
     * ======================== Exercise 3, Part B — Analysis =======================
     * Let n be the number of lockers.
     *
     * Q1. Which structure did you use (TreeSet, TreeMap, or both), what does it
     *     store, and why does it fit this problem?
     *
     * Q2. If you had stored the free lockers in an unsorted ArrayList instead,
     *     what would be the worst-case running time of assign and of release?
     *
     * Q3. What is the worst-case running time of assign and release with your
     *     solution? Justify it using the fact that TreeSet and TreeMap are
     *     red-black trees.
     * ============================================================================
     */
}
