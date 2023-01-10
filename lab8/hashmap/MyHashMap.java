package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private Set<K> allKeys;
    private double loadFactor;
    private int capacity;
    private int size;

    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int DEFAULT_CAPACITY = 16;

    /** Constructors */
    public MyHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, DEFAULT_LOAD_FACTOR);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.capacity = initialSize;
        this.loadFactor = maxLoad;
        this.size = 0;
        this.buckets = createTable(this.capacity);
        for (int idx = 0; idx < this.capacity; idx++) {
            this.buckets[idx] = createBucket();
        }
        this.allKeys = new HashSet<>();
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    /** Removes all of the mappings from this map. */
    @Override
    public void clear() {
        for (int idx = 0; idx < this.capacity; idx++) {
            buckets[idx].clear();
        }
        this.size = 0;
        this.allKeys.clear();
    }

    /** Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        return allKeys.contains(key);
    }

    /** Calculate the hash code for the specified key. */
    private int calcHash(K key) {
        int hash = key.hashCode();
        return Math.floorMod(hash, this.capacity);
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        Node node = getNode(key);
        if (node == null) {
//            System.out.println("No mapping with that key.");
            return null;
        }
        return node.value;
    }

    private Node getNode(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        int bucketNumber = calcHash(key);
        for (Node node : buckets[bucketNumber]) {
            if (node.key.equals(key)) {
                return node;
            }
        }
        return null;
    }

    /** Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced.
     */
    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key argument is null.");
        }

        Node node = getNode(key);
        if (node == null) {
            int hash = calcHash(key);
            Node newNode = new Node(key, value);
            buckets[hash].add(newNode);
            this.size++;
            allKeys.add(key);
            if (this.currentLoad()) {
                resize(this.capacity * 2);
            }
        } else {
            node.value = value;
        }
    }

    /** Calculates the current load factor and returns true if too large. */
    private boolean currentLoad() {
       return ((double) this.size / this.capacity) > this.loadFactor;
    }

    /** Grows the HashMap */
    private void resize(int newCapacity) {
        MyHashMap<K, V> newHashMap = new MyHashMap<>(newCapacity, this.loadFactor);
        for (K key : allKeys) {
            newHashMap.put(key, get(key));
        }
        this.buckets = newHashMap.buckets;
        this.capacity = newHashMap.capacity;
    }

    /** Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        return this.allKeys;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.
     */
    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

}
