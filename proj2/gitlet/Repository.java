package gitlet;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    }

    /** Checkout command that, depending on its arguments, will call a different
     * method. It changes the working directory either by setting it to the
     * designated branch or by overwriting a file with one belonging to another commit.
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
            // Ensure commitID exists

            checkoutFile(args[1], args[3]);
        }
        // checkout [branch name]
        else if (args.length == 2) {
            // Is the branch already checked out?
            if (args[1].equals(HEAD.getCurrentHead())) {
                exitMsg("No need to check out the current branch.");
            }

            // Does the branch not exist?
            List<String> branches = Utils.plainFilenamesIn(Branch.BRANCHES_DIR);
            if (!branches.contains(args[1])) {
                exitMsg("No such branch exists.");
            }

            /* Is there an untracked working file that would be overwritten? */
            // Create a list of the names of files in the working directory.
            List<String> workingFiles = Utils.plainFilenamesIn(CWD);
            // Load the current commit in order to access its HashMap of blobs.
            Commit headCommit = Commit.loadCommit(HEAD.getHeadID());

            for (String filename : workingFiles) {
                if (headCommit.getBlobName(filename) == null) {
                    exitMsg("There is an untracked file in the way; delete it, or add and commit it first.");
                }
                // File is tracked by current commit, so compare contents.
                String committedContents = headCommit.getCommittedFileContents(filename);
                String workingContents = Utils.readContentsAsString(Utils.join(CWD, filename));
                if (!committedContents.equals(workingContents)) {
                    exitMsg("There is an untracked file in the way; delete it, or add and commit it first.");
                }
            }

            /* Takes all files in the commit at the head of the given branch, and puts them in
             the working directory, overwriting the versions of the files that are already there
             if they exist. Any files that are tracked in the current branch but are not
             present in the checked-out branch are deleted. The staging area is cleared. */
            Commit checkoutCommit = Commit.loadCommit(Branch.getBranchHead(args[1]));
            HashMap<String, String> checkoutBlobs = checkoutCommit.getBlobs();
            /* Iterate over the current commit's tracked files. If they are not also tracked by checkout-commit,
            then delete from the working directory. */
            HashMap<String, String> currentBlobs = headCommit.getBlobs();
            for (Map.Entry<String, String> entry : currentBlobs.entrySet()) {
                String currentBranchFileName = entry.getKey();
                if (!checkoutBlobs.containsKey(currentBranchFileName)) {
                    // Not tracked by the checked-out branch, so delete from working directory.
                    Utils.join(CWD, currentBranchFileName).delete();
                }
            }

            /* Iterate over the checked-out branch blobs and write them to the working directory. */
            for (Map.Entry<String, String> entry : checkoutBlobs.entrySet()) {
                byte[] contents = Blob.readBlob(entry.getValue());
                Utils.writeContents(Utils.join(CWD, entry.getKey()), (Object) contents);
            }

            // Clear staging area
            Index index = Index.load();
            index.clear();

            // Update HEAD to reference the now checked out branch.
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
        // Load the specified commit.
        Commit commit = Commit.loadCommit(commitID);
        if (commit == null) {
            exitMsg("No commit with that id exists.");
        }
        // Find blob id.
        String blobName = commit.getBlobName(fileName);
        if (blobName == null) {
            exitMsg("File does not exist in that commit.");
        }
        // Load the blob.
        byte[] contents = Blob.readBlob(blobName);
        // Write to file in working directory.
        Utils.writeContents(Utils.join(CWD, fileName), (Object) contents);
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
