import java.util.Objects;

public class ProbeHashMap<K, V> {
    private static final Object AVAILABLE = new Object();   // tombstone
    private Object[] keys;
    private Object[] vals;
    private int n = 0;

    // Makes an empty probe hash map with the given capacity
        // Then the keys and values arrays are allocated to hold capacity entries
    public ProbeHashMap(int capacity) { keys = new Object[capacity]; vals = new Object[capacity]; }

    // Returns the initial hash index for the given key
        // The key's hashCode is made non-negative and reduced by mod the table length
    private int h(Object key) { return (key.hashCode() & 0x7fffffff) % keys.length; }

    // Searches for the given key using linear probing
        // Skips tombstones, stops at a never-used null slot, returning the index of the key if found
            // -1 if absent
    private int find(Object key) {
        int i = h(key);
        for (int p = 0; p < keys.length; p++) {
            if (keys[i] == null) return -1;                     // empty: stop
            if (keys[i] != AVAILABLE && keys[i].equals(key)) return i;
            i = (i + 1) % keys.length;                          // next cell
        }
        return -1;
    }

    // Returns the value associated with the given key, or null if the key is absent
    public V get(K key) { int i = find(key); return i < 0 ? null : (V) vals[i]; }

    // Inserts or updates the mapping for the given key
        // If the key already exists, replaces its value and returns the old value
            // Otherwise, inserts it into the first free or reusable tombstone slot and returns null
    public V put(K key, V value) {
        int i = find(key);
        if (i >= 0) { V old = (V) vals[i]; vals[i] = value; return old; }
        if (n == keys.length) throw new IllegalStateException("table full");
        i = h(key);
        while (keys[i] != null && keys[i] != AVAILABLE)          // first free cell
            i = (i + 1) % keys.length;
        keys[i] = key; vals[i] = value; n++;
        return null;
    }

    // Returns a string representation of the table
        // "_" means it was never used
        // "A" means tombstone
        // Other entries show their keys
    public String dump() {
        StringBuilder sb = new StringBuilder();
        for (Object k : keys) sb.append(k == null ? "_" : k == AVAILABLE ? "A" : k.toString()).append(' ');
        return sb.toString();
    }

    /**
     * Removes the entry associated with key, if present.
     *
     * @param key the key whose entry should be removed
     * @return the removed value, or null if the key is not found
     *
     * After a successful removal:
     * - Mark the tombstone.
     * - Return the stored value.
     * - Update the number of active entries.
     * Other entries must remain reachable through probing.
     */
    public V remove(K key) {
        // Find the slot holding the key
        int i = find(key);
        // Null if not found
        if (i < 0) return null;
        // Save the value to return
        V old = (V) vals[i];
        // Mark the slot as a tombstone so probing still works for other keys
        keys[i] = AVAILABLE;
        // Clear the value reference
        vals[i] = null;
        // One fewer active entry, return removed value
        n--;
        return old;
    }

    /**
     * Removes the entry associated with key only if its current
     * value equals the specified value.
     *
     * @param key the key whose entry should be removed
     * @param value the value that must match the stored value
     * @return true if the entry was removed; false otherwise
     *
     * If the key is absent or its value does not match, leave the
     * map unchanged. A successful removal must use a tombstone
     * and update the number of active entries.
     * Compare values by equality, including possible null values.
     */
    public boolean remove(K key, V value) {
        // Find the slot holding the key
            // Negative if not found
        int i = find(key);
        // If no key, return false - nothing to remove
        if (i < 0) return false;
        // If value doesn't match, return false - leave the map alone
        if (!Objects.equals(vals[i], value)) return false;
        // Mark the slot as a tombstone so probing will still work for other keys
        keys[i] = AVAILABLE;
        // Clear value reference
        vals[i] = null;
        n--;
        // Removal occurred
        return true;
    }

    /**
     * Replaces the value associated with key only if the key
     * is already present in the map.
     *
     * @param key the key whose value should be replaced
     * @param value the new value to store
     * @return true if the replacement occurred; false otherwise
     *
     * If the key is absent, leave the map unchanged.
     * Do not insert a new entry or change the number of entries.
     * A key mapped to null is still considered present.
     */
    public boolean replace(K key, V value) {
        // Find the slot holding the key
            // Negative if not found
        int i = find(key);
        // If key isn't present, return false - nothing to replace
        if (i < 0) return false;
        // Overwrite the stored value - entry count stays the same
        vals[i] = value;
        // Replacement occurred
        return true;
    }

    /**
     * Replaces the value associated with key with newValue only
     * if the key is present and its current value equals oldValue.
     *
     * @param key the key whose value should be replaced
     * @param oldValue the value that must match the stored value
     * @param newValue the new value to store
     * @return the previous value if replacement occurred;
     *         null otherwise
     *
     * If the key is absent or its value does not match, leave the
     * map unchanged. Do not change the number of entries.
     * Compare values by equality, including possible null values.
     * A successful replacement of a null value also returns null.
     */
    public V replace(K key, V oldValue, V newValue) {
        // Find the slot holding the key
            // Negative if not found
        int i = find(key);
        // If key isn't present return null - nothing to replace
        if (i < 0) return null;
        // If current value doesn't match, return null - leave the map alone
        if (!Objects.equals(vals[i], oldValue)) return null;
        // Save the current value so it can be returned
        V old = (V) vals[i];
        // Overwrite the stored value - entry count stays the same
        vals[i] = newValue;
        return old;
    }

    /*
     * TODO: Explain the role of tombstones:
     * - How does a tombstone differ from a never-used slot?
     * - Why can replacing a deleted key with null break searches?
     * - How do searches and insertions handle tombstones?
     * Include a short example with colliding keys and a deletion.
     */
    // A tombstone marks a slot whose entry has been deleted, but the sequence will keep probing past it.
    // Whereas a never-used slot means that the probe sequence can stop at the slot. If a deleted key were
    // replaced with null, a search for another colliding key later in the same probe sequence could stop
    // too early and incorrectly report that key as absent. Searches skip tombstones and continue until they
    // find the key or reach a never-used slot, and insertions may reuse tombstone slots as free space.
    // For example, a table with capacity 7, keys 1, 8, and 15 all hash to index 1. If they are inserted in
    // that order, 1, 8, and 15 occupy indices 1, 2, and 3. If key 8 is deleted, index 2 becomes a tombstone,
    // and a search for key 15 must skip the tombstone at index 2 and continue to index 3. Therefore, if
    // index 2 had been set to null, the search would stop and fail, then a later insertion (like key 22)
    // can reuse the tombstone slot at index 2.

    // Test for the ProbeHashMap implementation.
    public static void main(String[] args) {
        System.out.println("ProbeHashMap tests");

        // Create a small map
            // Cap 7 so collisions are easily triggered
        ProbeHashMap<Integer, String> map = new ProbeHashMap<>(7);

        // Insert 3 keys that all hash to the same slot (1, 8, 15 with cap 7)
        System.out.println("put 1 -> one: expected null, actual " + map.put(1, "one"));
        System.out.println("put 8 -> eight: expected null, actual " + map.put(8, "eight"));
        System.out.println("put 15 -> fifteen: expected null, actual " + map.put(15, "fifteen"));

        // Both should be found even though they collided with key 1
        System.out.println("collision get 8: expected eight, actual " + map.get(8));
        System.out.println("collision get 15: expected fifteen, actual " + map.get(15));
        System.out.println("dump before remove: " + map.dump());

        // Remove the middle of the probe chain and leave a tombstone
        System.out.println("remove 8: expected eight, actual " + map.remove(8));
        // Key 15 must still be reachable by probing past the tombstone
        System.out.println("get 15 after tombstone: expected fifteen, actual " + map.get(15));
        // Removed key should be gone
        System.out.println("get 8 after remove: expected null, actual " + map.get(8));
        System.out.println("dump with tombstone: " + map.dump());

        // Insert another colliding key; it should reuse the tombstone slot
        System.out.println("put 22 -> twenty-two (reuse tombstone): expected null, actual " + map.put(22, "twenty-two"));
        System.out.println("get 22: expected twenty-two, actual " + map.get(22));
        // Existing key 15 must still be found after the reuse
        System.out.println("get 15 after reuse: expected fifteen, actual " + map.get(15));
        System.out.println("dump after reuse: " + map.dump());

        // Overwrite using replace(key, value) only if the key exists
        System.out.println("replace 1 with ONE: expected true, actual " + map.replace(1, "ONE"));
        System.out.println("get 1: expected ONE, actual " + map.get(1));

        // Use replace on a missing key shouldn't do anything - return false
        System.out.println("replace 999 with X: expected false, actual " + map.replace(999, "X"));

        // Swapping using replace(key, old, new) only when the current value matches
        System.out.println("replace 1 old ONE new uno: expected ONE, actual " + map.replace(1, "ONE", "uno"));
        System.out.println("get 1: expected uno, actual " + map.get(1));

        // Wrong old value - no change, return null
        System.out.println("replace 1 old WRONG new bad: expected null, actual " + map.replace(1, "WRONG", "bad"));
        System.out.println("get 1: expected uno, actual " + map.get(1));

        // remove(key, value) with a wrong value - no change, return false
        System.out.println("remove 1 with wrong value: expected false, actual " + map.remove(1, "WRONG"));
        System.out.println("get 1: expected uno, actual " + map.get(1));

        // remove(key, value) with the right value - removes and returns true
        System.out.println("remove 1 with correct value: expected true, actual " + map.remove(1, "uno"));
        System.out.println("get 1: expected null, actual " + map.get(1));

        // remove on a missing key returns null
        System.out.println("remove 999: expected null, actual " + map.remove(999));
    }
}
