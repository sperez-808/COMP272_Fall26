package cache;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.Test;

/**
 * Extra tests for a solution with the LRU policy (least recently used).
 * To use them, make your test class extend LruPolicyTest instead of PageCacheTest:
 * it then runs these tests AND all the tests of PageCacheTest.
 *
 * <p>In these tests, a "use" of a page is a get that hits, or a put of a page already
 * present.
 */
abstract class LruPolicyTest extends PageCacheTest {

    @Test
    void theLeastRecentlyUsedPageIsEvicted() {
        PageCache cache = createCache(3);
        cache.put("A", "a");
        cache.put("B", "b");
        cache.put("C", "c");
        cache.put("D", "d");   // A is the least recently used
        assertFalse(cache.contains("A"));
        assertTrue(cache.contains("B"));
        assertTrue(cache.contains("C"));
        assertTrue(cache.contains("D"));
    }

    @Test
    void aHitMakesThePageMostRecentlyUsed() {
        PageCache cache = createCache(3);
        cache.put("A", "a");
        cache.put("B", "b");
        cache.put("C", "c");
        cache.get("A");        // now B is the least recently used
        cache.put("D", "d");
        assertTrue(cache.contains("A"));
        assertFalse(cache.contains("B"));
    }

    @Test
    void containsDoesNotChangeTheRecency() {
        PageCache cache = createCache(2);
        cache.put("A", "a");
        cache.put("B", "b");
        cache.contains("A");   // must NOT count as a use
        cache.put("C", "c");
        assertFalse(cache.contains("A"));
    }

    @Test
    void randomStreamGivesTheSameHitsAsLinkedHashMap() {
        // java.util.LinkedHashMap with accessOrder = true keeps its entries in LRU order.
        // It is used here only as a REFERENCE to check your answers.
        int capacity = 100;
        Map<String, String> reference = new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return size() > capacity;
            }
        };
        PageCache cache = createCache(capacity);
        Random random = new Random(7);
        for (int i = 0; i < 100_000; i++) {
            String page = "Page_" + random.nextInt(300);
            boolean expectedHit = reference.get(page) != null;
            if (!expectedHit) {
                reference.put(page, "content");
            }
            assertEquals(expectedHit, request(cache, page), "request " + i + ": " + page);
        }
    }
}
