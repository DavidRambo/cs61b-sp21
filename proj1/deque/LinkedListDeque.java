package deque;

/**
 * Linked List Deque class implementation.
 * CS61B Spring 2021 Project 1
 * author: David Rambo
 * */

public class LinkedListDeque<T> {
    private class Node {
        public Node prev;
        public T item;
        public Node next;

        public Node(Node p, T i, Node n) {
            prev = p;
            item = i;
            next = n;
        }
    }

    private Node sentinel = new Node(null, null, null);
    private int size;

    /** Creates an empty LLD. */
    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel.prev = sentinel;
        size = 0;
    }

    /** Constructor method for a LLD with a circular sentinel node. */
    public LinkedListDeque(T i) {
        Node first;
        first = new Node(sentinel, i, sentinel);
        sentinel.next = first;
        sentinel.prev = first;
        size = 1;
    }

    /** Returns the size of the deque. */
    public int size() {
        return size;
    }

    /** Returns true if deque is empty. */
    public boolean isEmpty() {
        return size == 0;
//        return sentinel.next == sentinel;
    }

    /** Helper method for adding the first/last item to an empty deque. */
    private void addNew(T item) {
        Node first = new Node(sentinel, item, sentinel);
        sentinel.next = first;
        sentinel.prev = first;
        size = 1;
    }

    /** Add new Node to the front of the deque. */
    public void addFirst(T item) {
        // Check whether this is first item being added.
        if (size == 0) {
            addNew(item);
        } else {
            Node oldFirst = sentinel.next;
            sentinel.next = new Node(oldFirst, item, sentinel);
            size += 1;
        }
    }

    /** Add new Node to the end of the deque.
     * */
    public void addLast(T item) {
        // Check whether this is first item being added.
        if (size == 0) {
            addNew(item);
        } else {
            // Access the end using sentinel.prev. Set new last item to have
            // its next = sentinel and its prev = the old last.
            Node oldLast = sentinel.prev;
            Node newLast = new Node(sentinel, item, oldLast);
            sentinel.prev = newLast;
            oldLast.next = newLast;
            size += 1;
        }
    }

    /** Prints the items in the deque from first to last, separated by a space.
     * Concludes with a new line.
     */
    public void printDeque() {
        Node p = sentinel.next;
        while (p != sentinel) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.println();
    }

    /** Removes and returns the item at the front of the deque.
     * If no such item exists, then it return s.null
     * @return Type
     */
    public T removeFirst() {
//        if (sentinel.next != sentinel) {
        if (!isEmpty()) {
            // Get item of first node.
            T firstItem = sentinel.next.item;
            // Set sentinel.next to point to second node.
            sentinel.next = sentinel.next.next;
            // Set that second node's prev to point to sentinel.
            sentinel.next.prev = sentinel;
            size -= 1;
            return firstItem;
        }
        return null;
    }

    /** Removes and returns the item at the front of the deque.
     * If no such item exists, then it return s.null
     * @return Type
     */
    public T removeLast() {
        if (!isEmpty()) {
            // Get last node's item for return value.
            T lastItem = sentinel.prev.item;
            // Create a name to point to second-to-last node.
            Node newLast = sentinel.prev.prev;
            // Set sentinel.prev to skip over last node to second-to-last node.
            sentinel.prev = newLast;
            // Set second-to-last's next to skip over last node to sentinel.
            newLast.next = sentinel;
            size -= 1;
            return lastItem;
        } else {
            return null;
        }
    }

    /** Returns the item at the given index, where 0 is the front.
     * If no such item exists, then return null. Does not alter the deque. */
    public T get(int index) {
        Node p = sentinel.next;
        while (index > 0) {
            if (p == sentinel) {
                return null;
            }
            p = p.next;
            index -= 1;
        }
        return p.item;
    }

    /** Recursive implementation of get method. */
    public T getRecursive(int index) {
        Node temp = sentinel.next;
        // Pass first node to the helper method.
        return getHelper(index, temp);
    }

    /** Helper method for getRecursive(). */
    private T getHelper(int index, Node p) {
        if (p.item == null) {
            return null;
        } else if (index == 0) {
            return p.item;
        } else {
            return getHelper(index - 1, p.next);
        }
    }

    // TODO: public Iterator<Type> iterator()
    // TODO: public boolean equals(Object o)
}
