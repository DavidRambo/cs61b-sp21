package tester;

import static org.junit.Assert.*;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

public class TestArrayDequeEC {

    @Test
    public void randomArrayTest() {
        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ads1 = new ArrayDequeSolution<>();
        int N = 20;

        StringBuilder errMsg = new StringBuilder();
        for (int i = 0; i < N; i ++) {
            int opNumber = StdRandom.uniform(0, 2);
            if (opNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                sad1.addLast(randVal);
                ads1.addLast(randVal);
                errMsg.append("addLast(").append(randVal).append(")\n");
                sad1.addLast(randVal + 1);
                ads1.addLast(randVal + 1);
                errMsg.append("addLast(").append(randVal + 1).append(")\n");
                errMsg.append("size()\n");
                assertEquals(String.valueOf(errMsg), sad1.size(), ads1.size());
            } else if (opNumber == 1) {
                // addFirst
                int randVal = StdRandom.uniform(0, 100);
                sad1.addFirst(randVal);
                ads1.addFirst(randVal);
                errMsg.append("addFirst(").append(randVal).append(")\n");
                sad1.addFirst(randVal + 1);
                ads1.addFirst(randVal + 1);
                errMsg.append("addFirst(").append(randVal + 1).append(")\n");
                errMsg.append("size()\n");
                assertEquals(String.valueOf(errMsg), sad1.size(), ads1.size());
            }
        }

        errMsg = new StringBuilder();
        for (int i = 0; i < N; i++) {
            int opNumber = StdRandom.uniform(0, 2);
            if (opNumber == 0 && sad1.size() > 0) {
                Integer sadFirst = sad1.removeFirst();
                Integer adsFirst = ads1.removeFirst();
                errMsg.append("removeFirst()\n");
                assertEquals(String.valueOf(errMsg), adsFirst, sadFirst);
            } else if (opNumber == 1 && sad1.size() > 0) {
                Integer sadLast = sad1.removeLast();
                Integer adsLast = ads1.removeLast();
                errMsg.append("removeLast()\n");
                assertEquals(String.valueOf(errMsg), adsLast, sadLast);
            }
        }
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests(TestArrayDequeEC.class);
    }
}
