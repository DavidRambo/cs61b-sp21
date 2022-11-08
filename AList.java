/**
 * Array based list.
 *
 * @author David Rambo
 */

/*
 * Invariants:
 * addLast: The next item we want to add wil go into position <size>.
 * getLast: The item we want to return is in position <size - 1>.
 * size: Then number of items in the List should be <size>.
 */

public class AList<Item> {
    private Item[] items;
    private int size;

    /** Creates an empty List. */
    public AList() {
        items = (Item[]) new Object[100];
        size = 0;
    }

    /** Inserts X into the back of the List. */
    public void addLast(Item x) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[size] = x;
        size += 1;
    }

    /** Returns the item from the back of the List. */
    public Item getLast() {
        return items[size - 1];
    }

    /** Gets the ith item in the List (0 is the front). */
    public Item get(int i) {
        return items[i];
    }

    /** Returns the number of items in the List. */
    public int size() {
        return size;
    }

    /** Deletes item from the back of the List and returns deleted item. */
    public Item removeLast() {
        Item returnItem = getLast();
        item[size - 1] = null;
        size -= 1;
        if ((size / items.length) < 0.25) {
            resize(size / 2);
        }
        return returnItem;
    }

    /** Resizes array to the target capacity. */
    public void resize(int capacity) {
        Item[] a = (Item[]) new Object[capacity];
        System.arraycopy(items, 0, a, 0, size);
        items = a;
    }
}
