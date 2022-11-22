package tester;

import static org.junit.Assert.*;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

public class TestArrayDequeEC {

    @Test
    public void randomArrayTest() {
        StudentArrayDeque<Integer> sad = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ads = new ArrayDequeSolution<>();

        int N = 1000;
        for (int i = 0; i < N; i ++) {
            int opNumber = StdRandom.uniform(0, 4);
            if (opNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                sad.addLast(randVal);
                ads.addLast(randVal);
                assertEquals(sad.size(), ads.size());
            } else if (opNumber == 1) {
                // addFirst
                int randVal = StdRandom.uniform(0, 100);
                sad.addFirst(randVal);
                ads.addFirst(randVal);
                assertEquals(sad.size(), ads.size());
            } else if (opNumber == 2) {
                // removeLast if not empty
                if (!ads.isEmpty()) {
                    int lastStudent = sad.removeLast();
                    int lastSolution = ads.removeLast();
                    assertEquals(lastSolution, lastStudent);
                }
            } else if (opNumber == 3) {
                // removeFirst if not empty
                if (!ads.isEmpty()) {
                    int lastStudent = sad.removeFirst();
                    int lastSolution = ads.removeFirst();
                    assertEquals(lastSolution, lastStudent);
                }
            }
        }
    }
}
