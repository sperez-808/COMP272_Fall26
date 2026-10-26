package cache;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Tests shared by both solutions, whatever their eviction policy. They are written
 * once, against the PageCache interface; Solution1Test and Solution2Test run them on
 * each solution.
 *
 * <p>Tests that depend on the policy are in LruPolicyTest and FifoPolicyTest.
 *
 * <p>These tests are a STARTING POINT. You must add more (see the TODO list at the end).
 */
abstract class PageCacheTest {

    /** Returns a new, empty cache of the solution being tested, with this capacity. */
    protected abstract PageCache createCache(int capacity);

    /** Simulates the proxy: get, and put on a miss. Returns true on a hit. */
    protected static boolean request(PageCache cache, String page) {
        if (cache.get(page) != null) {
            return true;
        }
        cache.put(page, "content of " + page);
        return false;
    }

    // ------------------------------------------------------------------
    // Construction and statistics
    // ------------------------------------------------------------------

    @Test
    void newCacheIsEmpty() {
        PageCache cache = createCache(10);
        assertEquals(0, cache.size());
        assertEquals(10, cache.capacity());
        assertEquals(0, cache.hits());
        assertEquals(0, cache.misses());
        assertEquals(0, cache.evictions());
        assertEquals(0.0, cache.hitRate(), 1e-9);
    }

    @Test
    void capacityBelowOneIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> createCache(0));
        assertThrows(IllegalArgumentException.class, () -> createCache(-5));
    }

    @Test
    void getOfAMissingPageReturnsNullAndCountsAMiss() {
        PageCache cache = createCache(10);
        assertNull(cache.get("Main_Page"));
        assertEquals(0, cache.hits());
        assertEquals(1, cache.misses());
    }

    @Test
    void getAfterPutReturnsTheContentAndCountsAHit() {
        PageCache cache = createCache(10);
        cache.put("Main_Page", "<html>main</html>");
        assertEquals("<html>main</html>", cache.get("Main_Page"));
        assertEquals(1, cache.hits());
        assertEquals(0, cache.misses());
    }

    @Test
    void containsDoesNotChangeTheStatistics() {
        PageCache cache = createCache(10);
        cache.put("A", "a");
        assertTrue(cache.contains("A"));
        assertFalse(cache.contains("B"));
        assertEquals(0, cache.hits());
        assertEquals(0, cache.misses());
    }

    @Test
    void hitRateIsHitsDividedByRequests() {
        PageCache cache = createCache(10);
        request(cache, "A");   // miss
        request(cache, "A");   // hit
        request(cache, "A");   // hit
        request(cache, "B");   // miss
        assertEquals(2, cache.hits());
        assertEquals(2, cache.misses());
        assertEquals(0.5, cache.hitRate(), 1e-9);
    }

    // ------------------------------------------------------------------
    // put and eviction
    // ------------------------------------------------------------------

    @Test
    void puttingAPagePresentReplacesItsContentWithoutEvicting() {
        PageCache cache = createCache(2);
        cache.put("A", "old");
        cache.put("B", "b");
        cache.put("A", "new");
        assertEquals(2, cache.size());
        assertEquals(0, cache.evictions());
        assertEquals("new", cache.get("A"));
    }

    @Test
    void exactlyOnePageIsEvictedWhenTheCacheIsFull() {
        PageCache cache = createCache(3);
        cache.put("A", "a");
        cache.put("B", "b");
        cache.put("C", "c");
        cache.put("D", "d");
        assertEquals(3, cache.size());
        assertEquals(1, cache.evictions());
        assertTrue(cache.contains("D"), "the new page must be in the cache");
        int stillThere = 0;
        for (String page : List.of("A", "B", "C")) {
            if (cache.contains(page)) {
                stillThere++;
            }
        }
        assertEquals(2, stillThere);
    }

    @Test
    void sizeNeverExceedsTheCapacity() {
        PageCache cache = createCache(10);
        for (int i = 0; i < 100; i++) {
            cache.put("Page_" + i, "content");
            assertTrue(cache.size() <= 10);
        }
        assertEquals(10, cache.size());
        assertEquals(90, cache.evictions());
    }

    @Test
    void cacheOfCapacityOneKeepsOnlyTheLastPage() {
        PageCache cache = createCache(1);
        cache.put("A", "a");
        cache.put("B", "b");
        assertFalse(cache.contains("A"));
        assertEquals("b", cache.get("B"));
        assertEquals(1, cache.size());
    }

    @Test
    void whenAllPagesFitEveryRepeatedRequestIsAHit() {
        PageCache cache = createCache(100);
        for (int round = 0; round < 5; round++) {
            for (int i = 0; i < 50; i++) {
                request(cache, "Page_" + i);
            }
        }
        assertEquals(50, cache.misses());       // only the first round misses
        assertEquals(200, cache.hits());
        assertEquals(0, cache.evictions());
    }

    // ------------------------------------------------------------------
    // Larger inputs
    // ------------------------------------------------------------------

    @Test
    void longRandomStreamKeepsTheCountersConsistent() {
        int capacity = 1_000;
        PageCache cache = createCache(capacity);
        Random random = new Random(42);   // fixed seed: the test is repeatable
        int requests = 200_000;
        for (int i = 0; i < requests; i++) {
            request(cache, "Page_" + random.nextInt(5_000));
            assertTrue(cache.size() <= capacity);
        }
        assertEquals(requests, cache.hits() + cache.misses());
        assertEquals(capacity, cache.size());
        // Every miss inserts one page; pages beyond the capacity were evicted.
        assertEquals(cache.misses() - capacity, cache.evictions());
    }

    // ------------------------------------------------------------------
    // Tests from the dataset
    // ------------------------------------------------------------------

    @Test
    @Disabled("Remove this line once DatasetReader works and a pageview file "
            + "(renamed pageviews-sample.gz) is in the data folder")
    void replayingTheDatasetKeepsTheCountersConsistent() throws IOException {
        List<DatasetReader.PageCount> counts =
                DatasetReader.readPageCounts(Path.of("data", "pageviews-sample.gz"), 100_000);
        String[] requests = DatasetReader.buildRequests(counts, 42);

        long totalViews = 0;
        for (DatasetReader.PageCount pc : counts) {
            totalViews += pc.views();
        }
        assertEquals(totalViews, requests.length);

        PageCache cache = createCache(1_000);
        for (String page : requests) {
            request(cache, page);
        }
        assertEquals(requests.length, cache.hits() + cache.misses());
        assertTrue(cache.size() <= 1_000);
    }

    // ------------------------------------------------------------------
    // TODO: add your own tests. At least:
    //  - Tests whose expected values YOU read from the dataset. For example: take the
    //    first 10 English lines of a pageview file, check that readPageCounts returns
    //    exactly those pages and counts, and that buildRequests contains each page the
    //    right number of times.
    //  - The same seed must give the same request stream; a different seed, a
    //    different order.
    //  - Page titles with special characters (accents, punctuation, very long titles).
    //  - The policy tests in LruPolicyTest / FifoPolicyTest (or a new test class for
    //    your policy, e.g. LFU).
    //  - Any special case of YOUR implementation (e.g. deleting in open addressing,
    //    evicting the head or tail of a linked list, ties in LFU).
    // ------------------------------------------------------------------
}
