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
    private final int n;
    private final TreeSet<Integer> free;

    /** Create lockers 1..n, all free. Throw IllegalArgumentException if n < 0. */
    public LockerAssigner(int n) {
        // If n < 0, throw fail with message
        if (n < 0) {
            throw new IllegalArgumentException("n must be non-negative");
        }
        // Remember the total number of lockers
        this.n = n;
        // 'free' holds locker ids that are unassigned
        // TreeSet keeps them sorted and cheaply retrievable
        this.free = new TreeSet<>();
        // Initially every locker is free, so add all to set
        for (int i = 1; i <= n; i++) {
            free.add(i);
        }
    }

    /**
     * Assign the free locker closest to preferred (ties: smaller number),
     * mark it as taken, and return its number. preferred may be any int,
     * even outside 1..n. Return -1 if no locker is free.
     */
    public int assign(int preferred) {
        // No lockers left to hand out - signal failure to the caller
        if (free.isEmpty()) {
            return -1;
        }
        // Find the nearest free lockers on each side of the preferred id
        Integer floor = free.floor(preferred);
        Integer ceiling = free.ceiling(preferred);
        int chosen;

        // If nothing free at or below the preference, take the next one up
        if (floor == null) {
            chosen = ceiling;
        // If nothing free at or above the preference, take the closest one down.
        } else if (ceiling == null) {
            chosen = floor;
        } else {
            // If both sides have a candidate, compare how far each is from the preferred id
            // Cast to long to avoid overflow on extreme values
            long floorDistance = (long) preferred - (long) floor;
            long ceilingDistance = (long) ceiling - (long) preferred;
            // Tie goes to the smaller locker, which is floor
            if (floorDistance <= ceilingDistance) {
                chosen = floor;
            } else {
                chosen = ceiling;
            }
        }
        // Mark the chosen locker as taken and return it to the caller
        free.remove(chosen);
        return chosen;
    }
}
    /**
     * Make locker free again and return true. Return false (and change
     * nothing) if it is already free or is not a number in 1..n.
     */
    public boolean release(int locker) {
        // Reject ids that dont correspond to any real locker, valid lockers are numbered
        if (locker < 1 || locker > n) {
            return false;
        }
        // Put the locker back into the free pool, set.add returns true only if locker wasn't already free
        return free.add(locker);
        }
    /*
     * ======================== Exercise 3, Part B — Analysis =======================
     * Let n be the number of lockers.
     *
     * Q1. Which structure did you use (TreeSet, TreeMap, or both), what does it
     *     store, and why does it fit this problem?
     * I used a TreeSet<Integer> because it stores exactly the number of the lcokers that are currently free.
     * The TreeSet keeps them sorted, so floor(x) gives the greatest free locker <= x and ceiling(x) gives the
     * smallest free locker >= x, and comparing their distances handles the nearest free requirement, and using
     * <= for the comparison gives the tie to the smallest locker.
     * 
     * Q2. If you had stored the free lockers in an unsorted ArrayList instead,
     *     what would be the worst-case running time of assign and of release?
     * If the free lockers were stored in an unsorted ArrayList, then assign would have to scan the whole list to
     * find the free locker closest to preferred. That is O(n) in the worst case, and removing the chosen locker from 
     * the ArrayList can also take O(n) because elements may need to be shifted. Release would also be O(n) because to
     * decide whether the locker is already free, the list could have to be scanned entirely. If it isn't free, adding it
     * at the end is O(1) amortized, but the worst case for release is still O(n) because of the scan.
     * 
     * Q3. What is the worst-case running time of assign and release with your
     *     solution? Justify it using the fact that TreeSet and TreeMap are
     *     red-black trees.
     * With TreeSet, assign is O(log n) worst case, and floor, ceiling, and remove are each O(log n) in a red-black
     * tree, and only a constant number of such operations are performed. Release is O(log n) worst case because add
     * on a TreeSet is O(log n) because TreeSet is backed by a red-black tree.
     * ============================================================================
     */
