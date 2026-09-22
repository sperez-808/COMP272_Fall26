import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * TicketQueue: a small "who's next" line for a customer-service desk.
 *
 * Customers are served in the order they arrive -- first in, first out
 * (FIFO). This class is backed by java.util.ArrayDeque, used through the
 * Queue interface: offer() adds at the back, poll() removes and returns
 * the front, peek() looks at the front without removing it.
 *
 * Notice what this class does NOT offer: there is no get(index), no way
 * to reach directly into the middle of the line. That is deliberate -- a
 * Queue ADT only promises FIFO access at the two ends, unlike the
 * SequenceList from Part 1, which promises indexed access everywhere.
 */
public class TicketQueue {

    private Queue<String> waiting;
    private int servedCount;

    public TicketQueue() {
        waiting = new ArrayDeque<>();
        servedCount = 0;
    }

    /** Adds a customer to the back of the line. */
    public void addCustomer(String customerId) {
        waiting.offer(customerId);
    }

    /** Returns the customer at the front without removing them, or null if empty. */
    public String peekNext() {
        return waiting.peek();
    }

    public int size() {
        return waiting.size();
    }

    public boolean isEmpty() {
        return waiting.isEmpty();
    }

    /** How many customers this queue has served so far via serveNext(). */
    public int getServedCount() {
        return servedCount;
    }

    /**
     * TODO 1: Remove and return the customer at the front of the line,
     * and increase servedCount by one.
     *
     * If the queue is empty, do NOT return null -- instead throw:
     *   throw new NoSuchElementException("No customers waiting");
     *
     * Hint: Queue's poll() returns null on an empty queue instead of
     * throwing, so check isEmpty() yourself before deciding what to do.
     */
    public String serveNext() {
        // TODO: implement
        // check if waiting is empty
        if (waiting.isEmpty()) {
            // if nobody is waiting, throw NoSuchElementException with message
            throw new NoSuchElementException("No customers waiting");
        }
        // if someone in line, update counter
        servedCount++;
        // retrieve and remove the next item with .poll()
        return waiting.poll();
    }

    /**
     * TODO 2: Return true if customerId is anywhere in the line (not
     * only at the front), without removing anyone or changing the
     * order of the line.
     *
     * Hint: Queue extends Iterable, so you can visit every element with
     * an enhanced for-loop:
     *   for (String id : waiting) { ... }
     * Reading the queue this way does not remove anything from it.
     */
    public boolean isCustomerWaiting(String customerId) {
        // TODO: implement
        // loop through waiting, store current customerId
        for (String id: : waiting) {
        // if a match is found, return true
            if (id.equals(customerId)){
                return true
            }
        }
        // otherwise return false
        return false
    }
}
