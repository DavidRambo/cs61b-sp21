package deque;

public class ArrayDeque<T> {
    private int size;
    private T[] items;
    private int front;
    private int back;

    /** Constructor class for ArrayDeque. */
    public ArrayDeque(T i) {
        items = (T[]) new Object[8];
        items[4] = i;
        front = 4;
        back = 4;
        size = 1;
    }

    /** Creates empty ArrayDeque. */
    public ArrayDeque() {
        items = (T[]) new Object[8];
        front = 4;
        back = 4;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    /** Adds an item to the front of the deque. */
    public void addFirst(T item) {
        // Check for availability in array.
        if (size == items.length) {
            resize(2 * size);
        }

        // Check for circular repositioning of front.
        if (front == 0) {
            front = items.length - 1;
        } else {
            front -= 1;
        }
        items[front] = item;
        size += 1;
    }

    /** Adds an item to the end of the deque. */
    public void addLast(T item) {
        // TODO
    }

    /** Removes the item at the front of the deque and returns it.
     * If no item exists, then returns null.
     */
    public T removeFirst() {
        // TODO
        return null;
    }

    /** Removes the item at the end of the deque and returns it.
     * If no item exists, then returns null.
     */
    public T removeLast() {
        // TODO
        return null;
    }

    /** Resizes the array.
     * Note that because the capacity changes by factors of 2,
     * capacity will always be divisible by 4. This integer is
     * used to reset the front position. */
    public void resize(int capacity) {
        T[] temp = (T[]) new Object[capacity];
        int start = capacity / 4;
        // For circular array, front will be at higher index than back.
        if (front > back) {
            // First, copy from front of deque to end of array.
            int end = items.length - 1;
            System.arraycopy(items, front, temp, start, end);
            // Calculate where to pick up for the copy operation.
            int secondStart = start + end;
            // Then, copy from beginning of array to back of deque.
            System.arraycopy(items, 0, temp, secondStart, back);
        } else {
            System.arraycopy(items, front, temp, start, size);
        }
        front = start;
        back = front + size;
        items = temp;
    }
}