package deque;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {

    @Test
    /* Construct an empty and a one-item Array Deque. */
    public void constructorTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>(5);
        ArrayDeque<Integer> ad2 = new ArrayDeque<>();
        assertTrue(ad2.isEmpty());
    }

    @Test
    /* Add to deque at front and back. Check whether empty.
     */
    public void addFrontAddLastTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>(5);
        ad1.addFirst(4);
        ad1.addLast(6);
        assertFalse(ad1.isEmpty());
    }

    @Test
    /* Add to deque to resize it. Check size.*/
    public void addResizeTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ad1.addFirst(4);
        ad1.addFirst(3);
        ad1.addFirst(2);
        ad1.addFirst(1);
        ad1.addFirst(0);
        ad1.addFirst(3);
        ad1.addFirst(2);
        ad1.addFirst(1);
        ad1.addFirst(0);
        ad1.addFirst(0);
        assertEquals(10, ad1.size());
    }

    @Test
    /* Add to front and back of deque to resize it. Check size. */
    public void frontBackResizeTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>(5);
        ad1.addFirst(4);
        ad1.addLast(6);
        ad1.addFirst(3);
        ad1.addFirst(2);
        ad1.addFirst(1);
        ad1.addFirst(0);
        ad1.addLast(7);
        ad1.addLast(8);
        ad1.addLast(9);
        ad1.addFirst(3);
        ad1.addFirst(2);
        ad1.addFirst(1);
        ad1.addFirst(0);
        ad1.addFirst(0);
        ad1.addLast(13);
        ad1.addLast(22);
        assertEquals(17, ad1.size());
    }

    @Test
    /* Adds items to the back to cause circular repositioning and resize. */
    public void addLastResizeTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>(0);
        ad1.addLast(1);
        ad1.addLast(2);
        ad1.addLast(3);
        ad1.addLast(4);
        ad1.addLast(5);
        ad1.addLast(6);
        ad1.addLast(7);
        ad1.addLast(8);
        ad1.addLast(9);
        assertEquals(10, ad1.size());
    }

    @Test
    /* Adds and removes items from empty, 1-item, and 2+ item deques.
    * Removes items to cause resize. */
    public void addRemoveTest() {
        ArrayDeque<Integer> adi = new ArrayDeque<>();
        assertNull(adi.removeFirst());
        assertNull(adi.removeLast());

        adi.addLast(0);
        int removed = adi.removeFirst();
        assertEquals(0, removed);
        assertNull(adi.removeFirst());
        assertNull(adi.removeLast());
        adi.addLast(1);
        removed = adi.removeLast();
        assertEquals(1, removed);
        assertTrue(adi.isEmpty());
        adi.addFirst(1);
        removed = adi.removeFirst();
        assertTrue(adi.isEmpty());
        assertEquals(1, removed);

        for (int i = 0; i < 25; i++) {
            if (i % 2 == 0) {
                adi.addFirst(i);
            } else {
                adi.addLast(i);
            }
        }
        assertEquals(25, adi.size());

        for (int i = 0; i < 18; i++) {
            adi.removeFirst();
        }
    }

    @Test
    /* Creates a deque with circularity and gets items. */
    public void getPrintTest() {
        ArrayDeque<Integer> adi = new ArrayDeque<>();
        adi.addLast(6);
        adi.addLast(7);
        adi.addFirst(5);
        adi.addFirst(4);
        adi.addFirst(3);
        adi.addFirst(2);
        adi.addFirst(1);
        adi.addFirst(0);
        // Array should look like: [2, 3, 4, 5, 6, 7, 0, 1]
        // Front = 6, Back = 5
        int getFront = adi.get(0);
        int getBack = adi.get(7);
        int getSecond = adi.get(1);
        assertEquals(0, getFront);
        assertEquals(7, getBack);
        assertEquals(1, getSecond);
        String testPrint = "0 1 2 3 4 5 6 7\n";
        System.out.println(testPrint);
        adi.printDeque();
    }
}