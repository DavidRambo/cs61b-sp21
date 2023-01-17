package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {  // If no commands are entered
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch(firstArg) {
            /* Creates a new Gitlet version-control system if one does not already exist. */
            case "init":
                /* Check whether .gitlet directory already exists. */
                if (Repository.GITLET_DIR.exists()) {
                    exitMessage("A Gitlet version-control system already exists in the current directory.");
                }
                Repository.init();
                break;
            /* Adds files to staging area. */
            case "add":
                if (args.length > 2) {
                    exitMessage("Incorrect operands.");
                }
                Repository.add(args[1]);
                break;
            case "rm" :
                if (args.length != 2) {
                    exitMessage("Incorrect operands.");
                }
                Repository.remove(args[1]);
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
            case "log":
                if (args.length != 1) {
                    exitMessage("Incorrect operands.");
                }
                Repository.log();
                break;
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
