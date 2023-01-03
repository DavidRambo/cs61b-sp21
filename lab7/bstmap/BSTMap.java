package bstmap;

import java.util.Iterator;
import java.util.Set;

/** BST class that implements the provided Map61B interface as its core data structure.
 * It implements all methods except for remove, iterator, and keySet.
 * In addition, it has a printInOrder() method, which prints out the BSTMap in order
 * of increasing key. */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    // The root of the BST.
    private BSTNode root;
    private int size;

    /* Creates an empty BSTMap by initializing the root as an empty BSTNode. */
    public BSTMap() {
        size = 0;
    }

    /** Removes all mappings. */
    @Override
    public void clear() {
        root = clear(root);
        size = 0;
    }

    private BSTNode clear(BSTNode node) {
        if (node == null) {
            return null;
        }
        node.left = clear(node.left);
        node.right = clear(node.right);
        return null;
    }

    /** Checks whether the BSTMap contains KEY.
     * Since this traverses the tree in the same way as GET,
     * and a non-null result returned by GET indicates the presence of the KEY,
     * simply return true if GET returns a non-null value. */
    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("cannot call with a null key");
        }
        return get(root, key) != null;
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("cannot call with a null key");
        }
        BSTNode node = get(root, key);
        if (node == null) {
            return null;
        } else {
            return node.val;
        }
    }

    /** Recursive method to find the BSTNode with key = KEY in the BSTMap. */
    private BSTNode get(BSTNode node, K key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return get(node.left, key);
        } else if (cmp > 0) {
            return get(node.right, key);
        }
        return node;
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("cannot call with a null key");
        }
        // Call private method to recursively traverse tree and return (modified) nodes.
        root = put(root, key, value);
    }

    /** Recursively traverses the tree to set the KEY, VALUE pair. */
    private BSTNode put(BSTNode node, K key, V value) {
        // Base case is that the correct node has been found, and it is null.
        // Return the new leaf node with KEY, VALUE pair.
        if (node == null) {
            this.size += 1;
            return new BSTNode(key, value);
        }
        // Otherwise, determine whether to go left or right OR to change this node's value.
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, value);
        } else if (cmp > 0) {
            node.right = put(node.right, key, value);
        } else {
            // must be the same key
            node.val = value;
        }
        return node;
    }

    /** Prints the BSTMap in order of increasing KEYs. */
    public void printInOrder() {
        if (root == null) {
            System.out.println("The BSTMap is empty.");
        } else {
            printInOrder(root);
        }
    }

    /** Recursively traverses the BSTMap's nodes, creating a chain of calls that resolves
     * once reaching a leaf. It puts off printing the @param node's key and value until
     * resolving nested calls.
     */
    private void printInOrder(BSTNode node) {
        // Check for smaller keys and go down that subtree if so.
        if (node.left != null) {
            printInOrder(node.left);
        }
        // Print out this node's key and value once the above traversal resolves.
        printNode(node);
        // Traverse subtree with larger keys.
        if (node.right != null) {
            printInOrder(node.right);
        }
    }

    private void printNode(BSTNode node) {
        System.out.print("Key: ");
        System.out.print(node.key);
        System.out.print("\tValue: ");
        System.out.println(node.val);
    }

    /** Represents one node in the BSTMap. Stores key as KEY and values as VAL.
     * Leaf nodes have both LEFT and RIGHT set to null.
     * SIZE is the number of nodes in its subtree. */
    private class BSTNode {
        private K key;
        private V val;
        private BSTNode left;
        private BSTNode right;

        /**
         * A leaf node.
         */
        public BSTNode(K k, V v) {
            key = k;
            val = v;
            left = null;
            right = null;
        }
    }

    /** Not required for this lab. */
    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    /** Not required for this lab. */
    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    /** Not required for this lab. */
    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    /* Not required for this lab. */
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        BSTMap<Integer, String> map = new BSTMap<>();
        map.put(4, "d");
        map.put(3, "c");
        map.put(2, "b");
        map.put(7, "g");
        map.put(9, "i");
        map.put(6, "f");

        map.printInOrder();
    }

}
