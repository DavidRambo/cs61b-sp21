package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    // Declare Comparator<T> object.
    private Comparator<T> c;

    /** Creates a MaxArrayDeque with the given Comparator. */
    public MaxArrayDeque(Comparator<T> cmp) {
        super();
        this.c = cmp;
    }

    /** Returns the maximum element in the deque as governed by the previously given Comparator.
     * If the MaxArrayDeque is empty, then returns null.
     */
    public T max() {
        return max(c);
    }

    /** Returns the maximum element in the deque as governed by the parameter Comparator c.
     * If the MaxArrayDeque is empty, then returns null.
     */
    public T max(Comparator<T> comparator) {
        if (isEmpty()) {
            return null;
        }

        T maxItem = get(0);  // Begin with front of deque.
        for (int i = 1; i < size(); i++) {
            int cmp = comparator.compare(get(i), maxItem);
            if (cmp > 0) {
                maxItem = get(i);
            }
        }
        return maxItem;
    }
}
