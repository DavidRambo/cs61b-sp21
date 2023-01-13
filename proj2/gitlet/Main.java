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
                Repository.init();
                break;
            /* Adds files to staging area. */
            case "add":
                Repository.addCommand(args);
                break;
            case "commit":
                Repository.commit(args);
                break;
            case "rm":
                // TODO: handle `rm [filename]` command
                break;
            case "log":
                // TODO: handle `log` command
                break;
            case "global-log":
                // TODO: handle `global-log` command
                break;
            case "find":
                // TODO: handle `find [filename]` command
                break;
            case "status":
                // TODO: handle `status` command
                break;
            case "checkout":
                Repository.checkoutCommand(args);
                break;
            case "branch":
                // TODO: handle `branch &opt [branch name]` command
                break;
            case "rm-branch":
                // TODO: handle `rm-branch` command
                break;
            case "reset":
                // TODO: handle `reset &opt [filename]` command
                break;
            case "merge":
                // TODO: handle `merge [commit name]` command
                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
        }
    }
}
