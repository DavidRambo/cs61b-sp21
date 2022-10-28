public class BreakContinue {
    /** Takes an array of integers n and another integer n. 
     * Replaces each element in a with the sum of that element a[i] through a[i+n],
     * but only if a[i] is positive. If there are not enough values in a, i.e. if
     * i+n > a.length, sum up to the end of the array.
    */
    public static void windowPosSum(int[] a, int n) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] < 1) {
                continue;
            } else {
                // places to go through the array
                int places = i + n;
                for (int j = i + 1; j <= places; j++) {
                    if (j >= a.length){
                        break;
                    } else {
                        a[i] = a[i] + a[j];
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] a = {1, 2, -3, 4, 5, 4};
        int n = 3;
        windowPosSum(a, n);

        // Should print 4, 8, -3, 13, 9, 4
        System.out.println(java.util.Arrays.toString(a));
    }
}