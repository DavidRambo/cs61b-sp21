package deque;

import org.junit.Test;
import static org.junit.Assert.*;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;

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

    @Test
    public void randomizedTest() {
        LinkedListDeque<Integer> L = new LinkedListDeque<>();
        ArrayDeque<Integer> A = new ArrayDeque<>();
        int size = 0;

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 5);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                A.addLast(randVal);
                size += 1;
                assertEquals(size, L.size());
            } else if (operationNumber == 1) {
                // addFirst and size
                int randVal = StdRandom.uniform(0, 100);
                L.addFirst(randVal);
                A.addFirst(randVal);
                size += 1;
                assertEquals(size, L.size());
                int sizeL = L.size();
                int sizeA = A.size();
                assertEquals(sizeL, sizeA);
            } else if (operationNumber == 2) {
                // get if size is non-zero
                if (L.size() > 1) {
                    int randIndex = StdRandom.uniform(0, L.size() - 1);
                    int getL = L.get(randIndex);
                    int getA = A.get(randIndex);
                    assertEquals(getL, getA);
                }
            } else if (operationNumber == 3) {
                // removeLast if size is non-zero
                L.removeLast();
                A.removeLast();
                if (size > 0) {
                    size -= 1;
                }
                assertEquals(size, L.size());
            } else if (operationNumber == 4) {
                L.removeFirst();
                A.removeFirst();
                if (size > 0) {
                    size -= 1;
                }
                assertEquals(size, L.size());
            }
        }
    }
}
