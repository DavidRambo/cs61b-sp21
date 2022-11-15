package deque;

import org.junit.Test;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Optional;

import static org.junit.Assert.*;


/** Performs some basic linked list tests. */
public class LinkedListDequeTest {

    @Test
    /** Creates a list, tries addFirst and addLast, and uses size() to confirm. */
    public void initializeTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        assertEquals(0, lld1.size());
//        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>(1);
//        assertEquals(1, lld1.size());
        lld1.addFirst(2);
        assertEquals(1, lld1.size());
        lld1.addLast(3);
        assertEquals(2, lld1.size());
    }

    @Test
    /** Tests the isEmpty() method. */
    public void isEmptyTest() {
        LinkedListDeque<String> llds = new LinkedListDeque<>();
        assertTrue(llds.isEmpty());
        LinkedListDeque<Integer> lldi = new LinkedListDeque<Integer>(5);
        assertFalse(lldi.isEmpty());

    }

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {

        LinkedListDeque<String> lld1 = new LinkedListDeque<String>();

		assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
		lld1.addFirst("front");

		// The && operator is the same as "and" in Python.
		// It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());

		lld1.addLast("middle");
		assertEquals(2, lld1.size());

		lld1.addLast("back");
		assertEquals(3, lld1.size());

		System.out.println("Printing out deque: ");
		lld1.printDeque();
    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
		// should be empty
		assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());

		lld1.addFirst(10);
		// should not be empty
		assertFalse("lld1 should contain 1 item", lld1.isEmpty());

		lld1.removeFirst();
		// should be empty
		assertTrue("lld1 should be empty after removal", lld1.isEmpty());
    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);
    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {

        LinkedListDeque<String>  lld1 = new LinkedListDeque<String>();
        LinkedListDeque<Double>  lld2 = new LinkedListDeque<Double>();
        LinkedListDeque<Boolean> lld3 = new LinkedListDeque<Boolean>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();
    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, lld1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, lld1.removeLast());

    }

    @Test
    /** Add some elements to the end of the deque.
     * Then remove from the end.
     */
    public void addLastRemoveLastTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>(1);
        lld1.addLast(2);
        lld1.addLast(3);
        lld1.addLast(4);
        assertEquals(4, lld1.size());

        int number = lld1.removeLast();
        assertEquals(4, number);
    }

    @Test
    /** Add several elements to deque, confirm correct order.
     * Remove element and confirm order is correct. */
    public void addOrderRemoveOrderTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
//        for (int i = 0; i < 10; i++) {
//            lld1.addFirst(i);
//        }
        for (int i = 0; i < 6; i += 2) {
            lld1.addLast(i);
        }
        System.out.println(">>> Printing deque...");
        lld1.printDeque();
        System.out.println(">>> Removing last...");
        int last = lld1.removeLast();
        assertEquals(4, last);
        System.out.println(last);
        System.out.println(">>> Printing deque...");
        lld1.printDeque();
        System.out.println(">>> Removing first...");
        System.out.println(lld1.removeFirst());
        System.out.println(">>> Printing deque...");
        lld1.printDeque();
//        assertEquals(8, lld1.size());
    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }

    }

    @Test
    /* Get non-existent item and then existent item. */
    public void getItemTest() {
        LinkedListDeque<Integer> lld = new LinkedListDeque(1);
        lld.addLast(2);
        lld.addLast(3);
        assertNull("No item at index, should be null.", lld.get(3));
        int value = lld.get(1);
        assertEquals("Item at index 1 should be 2.", 2, value);
    }

    @Test
    /* Use getRecursive to get non-existent and existent items. */
    public void getItemRecursiveTest() {
        LinkedListDeque<Integer> lld = new LinkedListDeque(1);
        lld.addLast(2);
        lld.addLast(3);
        assertNull("No item at index, should be null.", lld.getRecursive(3));
        int value = lld.getRecursive(1);
        assertEquals("Item at index 1 should be 2.", 2, value);
        // Confirm size is the same.
        assertEquals(3, lld.size());
    }

    @Test
    /* Randomly adds to and removes from deque. */
    public void randomLLDequeTest() {
        LinkedListDeque<Integer> lld = new LinkedListDeque<>();
        int size = 0;

        int N = 1000;
        for (int i = 0; i < N; i++) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                int randVal = StdRandom.uniform(0, 100);
                lld.addFirst(randVal);
                size += 1;
                assertEquals(size, lld.size());
            } else if (operationNumber == 1) {
                int randVal = StdRandom.uniform(0, 100);
                lld.addLast(randVal);
                size += 1;
                assertEquals(size, lld.size());
            } else if (operationNumber == 2) {
                lld.removeFirst();
                if (size > 0) {
                    size -= 1;
                }
                assertEquals(size, lld.size());
            } else if (operationNumber == 3) {
                lld.removeLast();
                if (size > 0) {
                    size -= 1;
                }
                assertEquals(size, lld.size());
            }
        }
    }
}
