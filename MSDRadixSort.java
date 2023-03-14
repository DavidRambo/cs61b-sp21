/** Exam Prep 13, #2: MSD Radix Sort
 * Implement the method msd, which runs MSD radix sort n a List of Strings
 * and returns a sorted List of Strings. Assume each string is of the same length.
 *
 * For the subroutine, you may use the stableSort method, which sorts the given list
 * of strings in place, comparing two strings by the given index. */

public static List<String> msd(List<String> items) {
    return  msd(items, 0);
}

/** This is the recursive implementation of MSD radix sort. Recursion is used to
 * handle sublists sharing top digit(s) as a separate problem. Use List's
 * subList(int fromIndex, int toIndex) method for this. Once the final 
 * character has been addressed, add to the answer array using List's 
 * addAll(Collection<? extends E> c) method. */
private static List<String> msd(List<String> items, int index) {
    /* Base cases:
     * 1. One or no Strings in the List.
     * 2. Partitioned list contains all of the same String. */
    if (items.size() <= 1 || index >= items.get(0).length()) {
        return items;
    }

    List<String> answer = new ArrayList<>();

    int start = 0;

    stableSort(items, index); // sort List by index-positioned character

    for (int end = 1; end <= items.size(); end += 1) {
        /* Compare index-positioned characters in strings to find partitions. */
        if (end == items.size() || 
                items.get(start).charAt(index) != (items.get(end).charAt(index))) {
            // 'end' is excluded from the subList.
            answer.addAll(msd(items.subList(start, end)), index + 1);
            start = end;
        }
    }
    return answer;
}


private static void stableSort(List<String> items, int index) {
    items.sort(Comparator.comparingInt(o -> o.charAt(index)));
}
