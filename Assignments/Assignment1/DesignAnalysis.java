/**
 * DesignAnalysis: there is no automatically-tested code here. Each
 * method below asks a multiple-choice question about cost or design.
 * Replace the "-1" in the return statement with the number of the
 * correct option, and write a one- or two-sentence justification in the
 * comment marked "Justification:", right above the return statement.
 *
 * These are graded by reading your answer and your justification, not
 * by running the code -- but every method must still compile and
 * return an int.
 */
public class DesignAnalysis {

    /**
     * Look back at add(T item) in LinkedSequenceList (Part 1) -- the
     * version given to you, not one of your TODOs. It has no tail
     * reference: every call walks all the way from head to the last
     * node before attaching the new node.
     *
     * What is the time complexity of calling add(item) once on a list
     * that already holds n elements?
     *
     *   1. O(1)
     *   2. O(log n)
     *   3. O(n)
     *   4. O(n^2)
     */
    public static int addWithoutTailCost() {
        // Justification:
        // (write your reasoning here)
        return 3; // replace with the correct option number
    }

    /**
     * Suppose LinkedSequenceList were changed to also keep a private
     * "tail" reference to its last node, kept correct on every add and
     * remove. What would add(item) become?
     *
     *   1. O(1)
     *   2. O(log n)
     *   3. O(n), same as before -- a tail reference would not help here
     *   4. O(n^2)
     */
    public static int addWithTailCost() {
        // Justification:
        // (write your reasoning here)
        return 1; // replace with the correct option number
    }

    /**
     * A ride-share app matches drivers to riders strictly in the order
     * ride requests arrive. A rider may cancel their request while they
     * are still waiting to be matched, at any position in the waiting
     * line, not only at the front.
     *
     * Which contract from this assignment best fits the "waiting to be
     * matched" part of this system?
     *
     *   1. SequenceList -- requests are read and removed by index
     *   2. A stack -- the most recent request should be matched first
     *   3. A queue -- requests are matched in arrival order
     *   4. None of the above; no ordered collection is needed
     */
    public static int rideShareScenario() {
        // Justification: name the operation that dominates this
        // workload, and say what a plain ArrayDeque-based queue gives
        // up once "cancel a request from the middle" is added as a
        // requirement.
        return 3; // replace with the correct option number
    }
}
