package cache;

/**
 * The page cache of Application C (web page cache for a proxy server).
 *
 * <p>This interface IS the functional requirements specification. Both of your
 * solutions ({@link Solution1} and {@link Solution2}) must implement it exactly
 * as documented here. Do not change the method names or signatures.
 *
 * <p>Each solution has a fixed capacity, given to its constructor, and an
 * eviction policy (LRU, FIFO, LFU, ...). The policy decides WHICH page is evicted
 * when a new page arrives and the cache is full. Document the policy of each
 * solution in its class comment, including what counts as a "use" of a page.
 * For example, with LRU both get (on a hit) and put (on a page already present)
 * usually count as a use; with FIFO, nothing changes the order except insertion.
 *
 * <p>The proxy uses the cache like this, for every request:
 * <pre>
 *     String content = cache.get(page);
 *     if (content == null) {                 // miss
 *         content = fetchFromWikipedia(page);
 *         cache.put(page, content);
 *     }
 * </pre>
 */
public interface PageCache {

    /**
     * Returns the content of the page if it is in the cache (a hit), or null (a miss).
     * Every call counts as exactly one hit or one miss. On a hit, the solution updates
     * whatever its policy needs (e.g. recency for LRU, frequency for LFU).
     */
    String get(String page);

    /**
     * Stores a page in the cache.
     * <ul>
     *   <li>If the page is already present, its content is replaced. Nothing is evicted.</li>
     *   <li>Otherwise, if the cache is full, exactly one page is evicted first,
     *       chosen by the policy, and the eviction counter increases by 1.</li>
     * </ul>
     * A put does not count as a hit or a miss.
     */
    void put(String page, String content);

    /**
     * Returns true if the page is in the cache. Unlike get, this does NOT count as a
     * hit or a miss and does NOT change the policy's information (recency, frequency).
     * It is used by tests and statistics.
     */
    boolean contains(String page);

    /** Returns the number of pages currently in the cache (never more than the capacity). */
    int size();

    /** Returns the maximum number of pages, as given to the constructor. */
    int capacity();

    /** Returns the number of calls to get that found the page. */
    long hits();

    /** Returns the number of calls to get that did not find the page. */
    long misses();

    /** Returns the number of pages evicted so far. */
    long evictions();

    /** Returns hits / (hits + misses), or 0.0 if get was never called. Already written for you. */
    default double hitRate() {
        long total = hits() + misses();
        return total == 0 ? 0.0 : (double) hits() / total;
    }
}
