package deque;

import java.util.Iterator;

/**
 * Linked List Deque class implementation.
 * CS61B Spring 2021 Project 1
 * author: David Rambo
 * */

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
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
    @Override
    public int size() {
        return size;
    }

    /** Helper method for adding the first/last item to an empty deque. */
    private void addNew(T item) {
        Node first = new Node(sentinel, item, sentinel);
        sentinel.next = first;
        sentinel.prev = first;
        size = 1;
    }

    /** Add new Node to the front of the deque. */
    @Override
    public void addFirst(T item) {
        // Check whether this is first item being added.
        if (isEmpty()) {
            addNew(item);
        } else {
            Node oldFirst = sentinel.next;
            sentinel.next = new Node(sentinel, item, oldFirst);
            oldFirst.prev = sentinel.next;
            size += 1;
        }
    }

    /** Add new Node to the end of the deque.
     * */
    @Override
    public void addLast(T item) {
        // Check whether this is first item being added.
        if (isEmpty()) {
            addNew(item);
        } else {
            // Access the end using sentinel.prev. Set new last item to have
            // its next = sentinel and its prev = the old last.
            Node oldLast = sentinel.prev;
            Node newLast = new Node(oldLast, item, sentinel);
            sentinel.prev = newLast;
            oldLast.next = newLast;
            size += 1;
        }
    }

    /** Prints the items in the deque from first to last, separated by a space.
     * Concludes with a new line.
     */
    @Override
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
    @Override
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
    @Override
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
    @Override
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

    // TODO: Test public Iterator<T> iterator()
    public Iterator<T> iterator() {
        return new LLDequeIterator();
    }
    private class LLDequeIterator implements Iterator<T> {
        private Node iterNode;

        public LLDequeIterator() {
            iterNode = sentinel.next;
        }

        public boolean hasNext() {
            return iterNode != sentinel;
        }

        public T next() {
            T returnItem = iterNode.item;
            iterNode = iterNode.next;
            return returnItem;
        }
    }

    @Override
    public boolean equals(Object o) {
        // For efficiency, check equality of reference.
        if (this == o) {
            return true;
        }

        if (o instanceof LinkedListDeque) {
            LinkedListDeque<T> otherLLD = (LinkedListDeque<T>) o;
            // Check that Deques are of equal size.
            if (otherLLD.size() != this.size()) {
                return false;
            }

            // Check that all of MY items are in the other Deque in the same order.
            // And b/c they are the same size, this will check all items.
            for (int i = 0; i < size(); i++) {
                T myItem = get(i);
                T otherItem = (T) otherLLD.get(i);
                if (!myItem.equals(otherItem)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
