public class SumArgs {
    public static void main(String[] args) {
        int N = args.length;
        int total = 0;

        for (String s : args) {
            int i = Integer.parseInt(s);
            total = total + i;
        }
        System.out.println(total);
    }
}
