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
                if (args.length > 2) {
                    exitMessage("Incorrect operands.");
                }
                Repository.add(args[1]);
                break;
            case "commit":
                if (args.length > 2) {
                    exitMessage("Incorrect operands.");
                }
                if (args.length == 1) {
                    exitMessage("Please enter a commit message.");
                }
                Repository.commit(args[1]);
                break;
            case "checkout":
                if (args.length == 3 && args[1].equals("--")) {
                    Repository.checkoutFile(args[2]);
                    break;
                } else if (args.length == 4 && args[2].equals("--")) {
                    Repository.checkoutFile(args[1], args[3]);
                    break;
                } else if (args.length == 2) {
                    Repository.checkoutBranch(args[1]);
                    break;
                } else {
                    exitMessage("Incorrect operands.");
                }
            default:
                exitMessage("No command with that name exists.");
        }
    }

    /** Template for handling errors. */
    public static void exitMessage(String message) {
        System.out.println(message);
        System.exit(0);
    }
}
