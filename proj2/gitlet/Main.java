package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                /* Check whether .gitlet directory already exists. */
                if (Repository.GITLET_DIR.exists()) {
                    exitMessage("A Gitlet version-control system already exists in the current directory.");
                }
                Repository.init();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                break;
            // TODO: FILL THE REST IN
        }
    }

    /** Template for handling errors. */
    private static void exitMessage(String message) {
        System.out.println(message);
        System.exit(0);
    }
}
