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
        ArrayDeque<Integer> ad1 = new ArrayDeque<>(5);
        ad1.addFirst(4);
//        ad1.addLast(6);
        ad1.addFirst(3);
        ad1.addFirst(2);
        ad1.addFirst(1);
        ad1.addFirst(0);
//        ad1.addLast(7);
//        ad1.addLast(8);
//        ad1.addLast(9);
        ad1.addFirst(3);
        ad1.addFirst(2);
        ad1.addFirst(1);
        ad1.addFirst(0);
        ad1.addFirst(0);
        assertEquals(10, ad1.size());
    }
}