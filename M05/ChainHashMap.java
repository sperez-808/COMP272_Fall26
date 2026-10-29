import java.util.LinkedList;

public class ChainHashMap<K, V> {

    private static class Entry<K, V> {
        final K key; 
        V value;
        Entry(K k, V v) { key = k; value = v; }
    }

    private LinkedList<Entry<K, V>>[] table;
    private int n = 0;                              // number of entries
    private static final double MAX_LOAD = 0.75;


    public ChainHashMap(int capacity) { table = new LinkedList[capacity]; }

    private int index(K key) {                      // hash code + compression
        return (key.hashCode() & 0x7fffffff) % table.length;
    }

    public V get(K key) {
        LinkedList<Entry<K, V>> bucket = table[index(key)];
        if (bucket == null) return null;
        for (Entry<K, V> e : bucket)
            if (e.key.equals(key)) return e.value;
        return null;
    }

    public V put(K key, V value) {
        int i = index(key);
        if (table[i] == null) table[i] = new LinkedList<>();
        for (Entry<K, V> e : table[i])
            if (e.key.equals(key)) { V old = e.value; e.value = value; return old; }
        table[i].add(new Entry<>(key, value));
        if (++n > MAX_LOAD * table.length) resize(2 * table.length + 1);
        return null;
    }

    public V remove(K key) {
        LinkedList<Entry<K, V>> bucket = table[index(key)];
        if (bucket == null) return null;
        for (Entry<K, V> e : bucket)
            if (e.key.equals(key)) { bucket.remove(e); n--; return e.value; }
        return null;
    }

    private void resize(int newCap) {               // rehash every entry: O(n)
        LinkedList<Entry<K, V>>[] old = table;
        table = new LinkedList[newCap];
        n = 0;
        for (LinkedList<Entry<K, V>> bucket : old)
            if (bucket != null)
                for (Entry<K, V> e : bucket) put(e.key, e.value);
    }

    public int size() { return n; }
}
