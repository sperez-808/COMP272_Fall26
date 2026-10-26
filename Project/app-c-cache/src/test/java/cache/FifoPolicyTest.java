package cache;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.Test;

/**
 * Extra tests for a solution with the FIFO policy (first in, first out).
 * To use them, make your test class extend FifoPolicyTest instead of PageCacheTest:
 * it then runs these tests AND all the tests of PageCacheTest.
 *
 * <p>With FIFO, the page evicted is the one that entered the cache first. A get or a
 * put of a page already present does not change the order.
 */
abstract class FifoPolicyTest extends PageCacheTest {

    @Test
    void theOldestPageIsEvicted() {
        PageCache cache = createCache(3);
        cache.put("A", "a");
        cache.put("B", "b");
        cache.put("C", "c");
        cache.put("D", "d");
        assertFalse(cache.contains("A"));
        assertTrue(cache.contains("D"));
    }

    @Test
    void aHitDoesNotSaveTheOldestPage() {
        PageCache cache = createCache(3);
        cache.put("A", "a");
        cache.put("B", "b");
        cache.put("C", "c");
        cache.get("A");        // with LRU this would save A; with FIFO it does not
        cache.put("D", "d");
        assertFalse(cache.contains("A"));
        assertTrue(cache.contains("B"));
    }

    @Test
    void randomStreamGivesTheSameHitsAsLinkedHashMap() {
        // java.util.LinkedHashMap with accessOrder = false keeps its entries in
        // insertion order. It is used here only as a REFERENCE to check your answers.
        int capacity = 100;
        Map<String, String> reference = new LinkedHashMap<>(16, 0.75f, false) {
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
