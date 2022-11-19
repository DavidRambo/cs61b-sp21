package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    // Declare Comparator<t> object.
    private Comparator<T> c;

    /** Creates a MaxArrayDeque with the given Comparator. */
    public MaxArrayDeque(Comparator<T> c) {
        super();
        this.c = c;
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
    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null;
        }

        T maxItem = get(0);  // Begin with front of deque.
        for (int i = 1; i < size(); i++) {
            int cmp = c.compare(get(i), maxItem);
            if (cmp > 0) {
                maxItem = get(i);
            }
        }
        return maxItem;
    }
}
