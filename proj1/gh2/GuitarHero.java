package gh2;
import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

public class GuitarHero {

    public static void main(String[] args) {
        String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
        GuitarString[] guitar = new GuitarString[keyboard.length()];

        for (int i = 0; i < keyboard.length(); i++) {
            double freq = 440 * Math.pow(2, (i - 24) / 12.0);
            guitar[i] = new GuitarString(freq);
        }

        while (true) {
            /* Check whether the user has typed a key. */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                /* Determine whether the pressed key corresponds to KEYBOARD. */
                int index = keyboard.indexOf(key);
                if (index != -1) {
                    guitar[index].pluck();
                }
            }

            /* Superposition of samples. */
            double sample = 0;
            for (int i = 0; i < keyboard.length(); i++) {
                sample += guitar[i].sample();
            }

            /* Play the sample on standard audio. */
            StdAudio.play(sample);

            /* Advance the simulation of each guitar string by one step. */
            for (int i = 0; i < keyboard.length(); i++) {
                guitar[i].tic();
            }
        }
    }
}
