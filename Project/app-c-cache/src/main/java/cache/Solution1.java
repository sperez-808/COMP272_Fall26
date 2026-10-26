package cache;

/**
 * Solution 1 for the page cache.
 *
 * <p>TODO: Replace this comment with a short description of your solution:
 * <ul>
 *   <li>the eviction policy (LRU, FIFO, LFU, ...) and what counts as a "use" of a page;</li>
 *   <li>the data structure(s) used (e.g. "a hash map from page to node, plus a doubly
 *       linked list ordered by recency");</li>
 *   <li>where the code comes from: written by your team, adapted from course code,
 *       or a library class (e.g. java.util.LinkedHashMap). Cite the source;</li>
 *   <li>the expected time complexity of get and put.</li>
 * </ul>
 * The full explanation of the implementation and of its expected performance
 * goes in your report.
 *
 * <p>You may rename this class to something more descriptive (e.g. LruCache).
 * If you do, also rename it in Solution1Test.
 */
public class Solution1 implements PageCache {

    // TODO: declare the fields of your data structure(s) here.

    /**
     * Creates an empty cache.
     *
     * @param capacity the maximum number of pages
     * @throws IllegalArgumentException if capacity is less than 1
     */
    public Solution1(int capacity) {
        // TODO: check the capacity and initialize your data structure(s).
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String get(String page) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void put(String page, String content) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean contains(String page) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int capacity() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public long hits() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public long misses() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public long evictions() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
