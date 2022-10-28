/** Draws a triangle out of asterisks in the console. */
public class DrawTriangle {
    public static void drawTriangle(int N) {
        int i = 0;
        while (i < N) {
            for (int j = 0; j < i; j++){
                System.out.print("*");
            }
            System.out.println("*");
            i++;
        }
    }

    public static void main(String[] args) {
        drawTriangle(10);
    }
}