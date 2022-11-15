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

    /** Returns true if deque is empty. */
    public boolean isEmpty() {
        return size == 0;
    }

    /** Returns the size of the deque as an int. */
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
        // Note that there is no risk of front meeting back due to above resize check.
        if (front == 0) {
            front = items.length - 1;
        } else if (!isEmpty()) {
            // Non-empty list, so move front.
            front -= 1;
        }
        items[front] = item;
        size += 1;
    }

    /** Adds an item to the end of the deque. */
    public void addLast(T item) {
        // Check whether array is full.
        if (size == items.length) {
            resize(2 * size);
        }

        // Check for circular repositioning of back.
        if (back == items.length - 1) {
            back = 0;
        } else if (!isEmpty()) {
            // Non-empty list, so move back.
            back += 1;
        }

        items[back] = item;
        size += 1;
    }

    /** Removes the item at the front of the deque and returns it.
     * If no item exists, then returns null.
     */
    public T removeFirst() {
        T removed = items[front];

        if (removed == null) {
            return null;
        } else if (front == items.length - 1 && front != back) {
            // Circular condition and not a one-item array.
            items[front] = null;
            front = 0;
        } else if (front != back) {
            items[front] = null;
            front += 1;
        }
        size -= 1;

        // Check usage ratio and resize if < 0.25
        // but do not shrink smaller than length of 8.
        if ((items.length > 8) && ((float) size/ items.length) < 0.25) {
            resize(items.length / 2);
        }
        return removed;
    }

    /** Removes the item at the end of the deque and returns it.
     * If no item exists, then returns null. */
    public T removeLast() {
        T removed = items[back];

        if (removed == null) {
            return null;
        } else if (back == 0 && front != back) {
            // Circular condition and more than one item in array.
            items[back] = null;
            back = items.length - 1;
        } else if (front != back) {
            // Check for 1-item array; i.e. front == back
            items[back] = null;
            back -= 1;
        }

        size -= 1;
        // Check usage ratio and resize if < 0.25
        // but do not shrink smaller than length of 8.
        if ((items.length > 8) && ((float) size/ items.length) < 0.25) {
            resize(items.length / 2);
        }
        return removed;
    }

    /** Gets the item at the given index, where 0 is the front, 1 the next item, etc.
     * If no such item exists, returns null.
     * In a non-circular array, location = front + index.
     * In a circular array, location can still be front + index, if that is < length.
     * If that is > length, then subtract length from it.
     * Example:
     * [2, 3, 4, 5, 6, 7, null, null, null, null, 0, 1]
     * front = 10, index = 6, length = 12
     * location = 4 -> items[4] = 6
     * 6 + 10 = 16 -> 16 - 12 = 4
     * */
    public T get(int index) {
        int location = front + index;
        // Check for circularity
        if (location > items.length) {
            location = location - items.length;
        }
        return items[location];
    }

    /** Prints the items in the deque from front to back, separated by a space. */
    public void printDeque() {
        int position = front;

        // Check for circularity.
        if (front > back) {
            // Print to end of array.
            while (position < items.length) {
                System.out.print(items[position] + " ");
                position += 1;
            }
            // Reset position to index 0 and print to back.
            position = 0;
        }

        while (position <= back) {
            System.out.print(items[position] + " ");
            position += 1;
        }

        System.out.println();
    }

    /** Resizes the array.
     * Note that because the capacity changes by factors of 2, and starting capacity is 8,
     * capacity will always be divisible by 4. This integer is used to reset the front position. */
    public void resize(int capacity) {
        T[] temp = (T[]) new Object[capacity];
        int start = capacity / 4;
        // For circular array, front will be at higher index than back.
        if (front > back) {
            // First, copy from front of deque to end of array.
            int numberOfItems = items.length - front;
            System.arraycopy(items, front, temp, start, numberOfItems);
            // Calculate where to pick up for the copy operation.
            int secondStart = start + numberOfItems;
            // Then, copy from beginning of array to back of deque.
            System.arraycopy(items, 0, temp, secondStart, (size - numberOfItems));
        } else {
            System.arraycopy(items, front, temp, start, size);
        }
        front = start;
        back = front + size - 1;
        items = temp;
    }
}