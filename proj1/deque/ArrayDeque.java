package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private int size;
    private T[] items;
    private int nextFirst;
    private int nextLast;

    /** Creates empty ArrayDeque. */
    public ArrayDeque() {
        items = (T[]) new Object[8];
        nextFirst = 4;
        nextLast = 5;
        size = 0;
    }

    /** Returns the index of the first item. */
    private int getFirstIndex() {
        return (nextFirst + 1) % items.length;
    }

    /** Returns the index of the last item. */
    private int getLastIndex() {
        return ((nextLast - 1) + items.length) % items.length;
    }

    /** Returns the size of the deque as an int. */
    @Override
    public int size() {
        return size;
    }

    /** Adds an item to the front of the deque. */
    @Override
    public void addFirst(T item) {
        // Check for availability in array.
        if (size == items.length) {
            resize(2 * size);
        }

        items[nextFirst] = item;
        size += 1;
        nextFirst = (nextFirst - 1 + items.length) % items.length;
    }

    /** Adds an item to the end of the deque. */
    @Override
    public void addLast(T item) {
        // Check whether array is full.
        if (size == items.length) {
            resize(2 * size);
        }

        items[nextLast] = item;
        size += 1;
        nextLast = (nextLast + 1) % items.length;
    }

    /** Removes the item at the front of the deque and returns it.
     * If no item exists, then returns null.
     */
    @Override
    public T removeFirst() {
        T removed = items[getFirstIndex()];

        if (removed == null) {
            return null;
        }

        items[getFirstIndex()] = null;
        nextFirst = (nextFirst + 1) % items.length;
        size -= 1;
        checkSize();

        return removed;
    }

    /** Removes the item at the end of the deque and returns it.
     * If no item exists, then returns null. */
    @Override
    public T removeLast() {
        T removed = items[getLastIndex()];

        if (removed == null) {
            return null;
        }

        items[getLastIndex()] = null;
        nextLast = ((nextLast - 1) + items.length) % items.length;
        size -= 1;
        checkSize();
        return removed;
    }

    /** Gets the item at the given index, where 0 is the front, 1 the next item, etc.
     * If no such item exists, returns null. */
    @Override
    public T get(int index) {
        int location = (getFirstIndex() + index) % items.length;
        return items[location];
    }

    /** Prints the items in the deque from front to back, separated by a space. */
    @Override
    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(get(i) + " ");
        }

        System.out.println();
    }

    /** Check usage ratio and resize if < 0.25, but do not shrink smaller than length of 8. */
    private void checkSize() {
        if ((items.length > 8) && ((float) size / items.length) < 0.25) {
            resize(items.length / 2);
        }
    }

    /** Resizes the array.
     * Note that because the capacity changes by factors of 2, and starting capacity is 8,
     * capacity will always be divisible by 4. This integer is used to reset the first position. */
    private void resize(int capacity) {
        T[] temp = (T[]) new Object[capacity];
        int start = capacity / 4;
        int first = getFirstIndex();
        int last = getLastIndex();
        // For circular array, first will be at higher index than last.
        if (first >= last) {
            // First, copy from first of deque to end of array.
            System.arraycopy(items, first, temp, start, items.length - first);
            // Calculate where to continue the copy operation in the new array.
            int secondStart = start + items.length - first;
            // Then, copy from beginning of original array to last of deque.
            System.arraycopy(items, 0, temp, secondStart, last + 1);
        } else {
            System.arraycopy(items, first, temp, start, size);
        }
        items = temp;
        nextFirst = (start - 1 + items.length) % items.length;
        nextLast = (nextFirst + size + 1) % items.length;
    }

    public Iterator<T> iterator() {
        return new ADequeIterator();
    }

    private class ADequeIterator implements Iterator<T> {
        private int iterPosition;

        public ADequeIterator() {
            iterPosition = 0;
        }

        public boolean hasNext() {
            return iterPosition != nextLast;
        }

        public T next() {
            T returnItem = items[iterPosition];
            iterPosition += 1;
            return returnItem;
        }
    }

    @Override
    public boolean equals(Object o) {
        // For efficiency, check equality of reference.
        if (this == o) { return true; }

        if (o instanceof Deque) {
            Deque<T> otherDeque = (Deque<T>) o;
            if (otherDeque.size() != this.size()) {
                return false;
            }

            // Check that all MY items are in the other array set in order.
            // And b/c they are the same size, this will match all items.
            for (int i = 0; i < size(); i++) {
                T myItem = get(i);
                T otherItem = otherDeque.get(i);
                if (!myItem.equals(otherItem)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}