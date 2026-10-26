package cache;

/**
 * Runs the tests on Solution2.
 *
 * <p>TODO: if Solution2 uses LRU, replace "extends PageCacheTest" with
 * "extends LruPolicyTest"; if it uses FIFO, with "extends FifoPolicyTest".
 * For another policy (e.g. LFU), write a new test class in the same style.
 * You may also add tests specific to Solution2 here.
 */
class Solution2Test extends PageCacheTest {

    @Override
    protected PageCache createCache(int capacity) {
        return new Solution2(capacity);
    }
}
