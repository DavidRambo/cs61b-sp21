package gitlet;

import java.io.File;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author David Rambo
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    /* TODO: fill in the rest of this class. */

    /** Init command to initialize the Gitlet version-control system in the current
        directory. If such a system already exists, it will instead print an error message.
        A newly initiated .gitlet repository looks like this:
         .gitlet/
          |-commits/
            |-initial commit
          |-refs/
            |-master
          |-blobs/
          |-index/
          |-HEAD */
    public static void init() {
        /* Checks for already existing .gitlet directory. */
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        // TODO: What is a UID?
        GITLET_DIR.mkdir();
        Commit.COMMITS_DIR.mkdir();
        Branch.BRANCHES_DIR.mkdir();
        Index.INDEX_DIR.mkdir();

        /* Create initial commit with the empty constructor. Then save it to filesystem. */
        Commit initial = new Commit();
        initial.writeCommit();
        /* Create master branch with initial commit. */
        Branch.updateCommit("master", initial.getCommitID());
        /* Set HEAD to point to the master branch. */
        HEAD.pointToBranch("master");
    }

    /** TODO: Status command. */
    public static String status(String[] args) {
        return "TODO";
    }

    /** TODO: Add command.
     * Calls a method in the Index class to add files in the working directory
     * to the staging area. */
    public static void addCommand(String[] args) {}

    /** Checkout command that, depending on its arguments, will call a
     * different method. It changes the working directory either by setting it
     * to the designated branch or by overwriting a file with one belonging
     * to another commit.
     * Passing `-- [file name]` to it serves as a kind of analogue to git's
     * restore command. */
    public static void checkoutCommand(String[] args) {
        // checkout -- [file name]
        if (args.length == 2 && args[1].equals("--")) {
            // Get the commit ID of the HEAD.
            String headID = Branch.getBranchHead(HEAD.getCurrentHead());
            checkoutFile(headID, args[2]);
        }
        // checkout [commit id] -- [file name]
        else if (args.length == 4 && args[2].equals("--")) {
            checkoutFile(args[1], args[3]);
        }
        // checkout [branch name]
        else if (args.length == 2) {
            HEAD.pointToBranch(args[1]);
        }
        // wrong number or format of arguments
        else {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }

    /** Takes the version of the file with the provided fileName as it exists in the
     * commit with the provided commitID and puts it into the working directory,
     * overwriting the version that is already there.
     * @param commitID ID of the commit with the requested file.
     * @param fileName name of the file to be checked out. */
    private static void checkoutFile(String commitID, String fileName) {
        // TODO
    }
}
