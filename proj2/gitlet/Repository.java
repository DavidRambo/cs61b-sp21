package gitlet;

import java.io.File;
import java.util.List;

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

    /** Initializes the Gitlet version-control system in the current directory. 
        If such a system already exists, it will instead print an error message.
        A newly initiated .gitlet repository looks like this:
         .gitlet/
          |-commits/
            |-initial commit
          |-refs/
            |-master
          |-blobs/
          |-index
          |-HEAD */
    public static void init() {
        /* Checks for already existing .gitlet directory. */
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists"
                    + " in the current directory.");
            System.exit(0);
        }
        GITLET_DIR.mkdir();

        // Create the directory to store commit objects.
        Commit.COMMITS_DIR.mkdir();

        // Create directory to store branch files.
        Branch.BRANCHES_DIR.mkdir();

        // Create directory to store blobs.
        Blob.BLOBS_DIR.mkdir();

        // Create the staging area
        Index index = new Index();
        index.save();

        /* Create HEAD file and set to point to the master branch. */
        Utils.writeContents(HEAD.HEAD_FILE, "master");

        /* Create initial commit with the empty constructor and save. */
        Commit initial = new Commit();
        initial.save();

        /* Create master branch with initial commit. */
        Branch.updateCommit("master", initial.getCommitID());
    }

    /** TODO: Status command. */
    public static String status(String[] args) {
        return "TODO";
    }

    /** Calls a method in Index.java to add files from the working directory
     * to the staging area. */
    public static void addCommand(String[] args) {
        if (args.length != 2) {
            exitMsg("Incorrect operands.");
        }
        // Ensure that the file exists.
        if (!fileExists(args[1])) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        // Load staging area
        Index index = Index.load();
        // Stage the file
        File file = Utils.join(CWD, args[1]);
        index.stageFile(args[1]);
    }

    /** Commit command. 
     * Usage: java Gitlet.commit [commit message] */
    public static void commit(String[] args) {
        // Validate number of args
        if (args.length != 2) {
            exitMsg("Incorrect operands.");
        }

        if (args[1].isEmpty()) {
            exitMsg("Please enter a commit message.");
        }

        // Read from Gitlet directory the head commit object.
        String headID = Branch.getBranchHead(HEAD.getCurrentHead());
        // Do the commit: message, parent commit, second parent commit.
        Commit newCommit = new Commit(args[0], headID, null);
        newCommit.save();

        // TODO: Update head of master of currently checked out branch.
    }

    /** Checkout command that, depending on its arguments, will call a
     * different method. It changes the working directory either by setting it
     * to the designated branch or by overwriting a file with one belonging
     * to another commit.
     * Passing `-- [file name]` to it serves as a kind of analogue to git's
     * restore command. */
    public static void checkoutCommand(String[] args) {
        // checkout -- [file name]
        // Note that I had first set args.length to 2, bit I think it should be 3.
        // args[0] == checkout, args[1] == --, args[2] == [file name]
        if (args.length == 3 && args[1].equals("--")) {
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
            exitMsg("Incorrect operands.");
        }
    }

    /** Takes the version of the file with the provided fileName as it exists in the
     * commit with the provided commitID and puts it into the working directory,
     * overwriting the version that is already there.
     * @param commitID ID of the commit with the requested file.
     * @param fileName name of the file to be checked out. */
    private static void checkoutFile(String commitID, String fileName) {
        // TODO
        throw new UnsupportedOperationException();
    }

    /** Checks whether the file exists in the CWD. 
     * @param fileName name of the file to check.
     * */
    private static boolean fileExists(String fileName) {
        // Create array of all plain file names against which to check.
        List<String> plainFiles = Utils.plainFilenamesIn(CWD);
        // Check for file in the list.
        if (plainFiles.contains(fileName)) {
            return true;
        }
        return false;
    }

    public static void exitMsg(String message) {
        System.out.println(message);
        System.exit(0);
    }
}
