package tester;

import static org.junit.Assert.*;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

public class TestArrayDequeEC {

    @Test
    public void randomArrayTest() {
        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<>();
        StudentArrayDeque<Integer> sad2 = new StudentArrayDeque<>();
        int size = 0;

        int N = 100;
        for (int i = 0; i < N; i ++) {
            int opNumber = StdRandom.uniform(0, 4);
            if (opNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                sad1.addLast(randVal);
                sad1.addLast(randVal + 1);
                Integer expected = randVal + 1;
                Integer actual = sad1.removeLast();
                assertEquals("addLast(" + randVal + ")\n" +
                        "addLast(" + expected + ")\n" +
                        "removeLast()", expected, actual);
                size += 1;
            } else if (opNumber == 1) {
                // addFirst
                int randVal = StdRandom.uniform(0, 100);
                sad1.addFirst(randVal);
                sad1.addFirst(randVal + 1);
                Integer expected = randVal + 1;
                Integer actual = sad1.removeFirst();
                assertEquals("addFirst(" + randVal + ")\n" +
                        "addFirst(" + expected + ")\n" +
                        "removeFirst()", expected, actual);
                size += 1;
            } else if (opNumber == 2) {
                int randVal = StdRandom.uniform(0, 100);
                sad1.addFirst(randVal);
                sad1.addLast(randVal + 1);
                sad2.addFirst(randVal);
                sad2.addLast(randVal + 1);
                size += 2;
                assertEquals("addFirst(" + randVal + ")\n" +
                        "addLast(" + (randVal + 1) + ")\n" +
                        "size()", sad1.size(), sad2.size());
            } else if (opNumber == 3) {
                if (size > 1) {
                    sad1.removeFirst();
                    sad1.removeLast();
                    sad2.removeFirst();
                    sad2.removeLast();
                    size -= 2;
                }
                int actual = sad1.size();
                assertEquals("removeFirst()\nremoveLast()\nsize()", sad1.size(), sad2.size());
            }
        }
    }
}
