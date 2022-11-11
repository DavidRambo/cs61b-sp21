package deque;

/**
 * Linked List Deque class implementation.
 * CS61B Spring 2021 Project 1
 * author: David Rambo
 * */

public class LinkedListDeque<Type> {
    private class Node {
        public Node prev;
        public Type item;
        public Node next;

        public Node(Node p, Type i, Node n) {
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
    public LinkedListDeque(Type i) {
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

    /** Add new Node to the front of the deque. */
    public void addFirst(Type item) {
        sentinel.next = new Node(sentinel, item, sentinel.next);
        size += 1;
    }

    /** Add new Node to the end of the deque.
     * */
    public void addLast(Type item) {
        // Reach the end using sentinel.prev. Set new last item to have
        // a next = sentinel and a prev = the old last.
        Node oldLast = sentinel.prev;
        sentinel.prev = new Node(oldLast, item, sentinel);
        oldLast.next = sentinel.prev;
        size += 1;
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
    public Type removeFirst() {
        if (sentinel.next != sentinel) {
            Type firstItem = sentinel.next.item;
            sentinel.next = sentinel.next.next;
            size -= 1;
            return firstItem;
        }
        return null;
    }

    /** Removes and returns the item at the front of the deque.
     * If no such item exists, then it return s.null
     * @return Type
     */
    public Type removeLast() {
        if (sentinel.prev != sentinel) {
            Type lastItem = sentinel.prev.item;
            Node newLast = sentinel.prev.prev;
            sentinel.prev = newLast;
            newLast.next = sentinel;
            size -= 1;
            return lastItem;
        }
        return null;
    }

    /** Returns the item at the given index, where 0 is the front.
     * If no such item exists, then return null. Does not alter the deque. */
    public Type get(int index) {
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
    public Type getRecursive(int index) {
        Node temp = sentinel.next;
        // Pass first node to the helper method.
        return getHelper(index, temp);
    }

    /** Helper method for getRecursive(). */
    private Type getHelper(int index, Node p) {
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
