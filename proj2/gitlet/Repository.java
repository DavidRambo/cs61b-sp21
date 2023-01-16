package gitlet;

import java.io.File;
import java.util.HashMap;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The directory in which to store blobs. */
    public static final File BLOBS_DIR = join(GITLET_DIR, "/blobs");
    /** The directory in which to store commits. */
    public static final File COMMITS_DIR = join(GITLET_DIR, "/commits");
    /** The file that stores branch names and their heads. */
    public static final File BRANCHES = join(GITLET_DIR, "/refs");
    /** The staging area file. */
    public static final File INDEX = join(GITLET_DIR, "index");
    /** The file that stores the currently checked out branch name. */
    public static final File HEAD = join(GITLET_DIR, "HEAD");

    /* TODO: fill in the rest of this class. */

    /** Creates a new Gitlet version-control system in the current directory.
     * This system will automatically start with one commit: a commit that
     * contains no files and has the commit message initial commit (just like that,
     * with no punctuation). It will have a single branch: master, which initially
     * points to this initial commit, and master will be the current branch. The
     * timestamp for this initial commit will be 00:00:00 UTC, Thursday, 1 January
     * 1970 in whatever format you choose for dates (this is called “The (Unix) Epoch”,
     * represented internally by the time 0.) Since the initial commit in all
     * repositories created by Gitlet will have exactly the same content, it follows
     * that all repositories will automatically share this commit (they will all
     * have the same UID) and all commits in all repositories will trace back to it.
     * .gitlet/
     * |–HEAD
     * |–index
     * |–blobs/
     *   |–blob objects
     * |–commits/
     *   |–commit objects
     * |–refs/
     *   |–branches
     * */
    public static void init() {
        /* Create directory structure. */
        GITLET_DIR.mkdir();
        BLOBS_DIR.mkdir();
        COMMITS_DIR.mkdir();
        BRANCHES.mkdir();

        /* Create the staging area. */
        Index index = new Index();
        index.save();

        /* Create the initial commit. */
        Commit commit = new Commit();
        commit.save();

        /* Create the HEAD file and write "master" to it. */
        updateHead("master");

        /* Create the file for the master branch and enter initial commit's ID. */
        String currentBranch = getCurrentBranch();
        updateBranchHead(currentBranch, commit.getID());
    }

    /** Adds file to the staging area.
     * If the current working version of the file is identical to the version in
     * the current commit, do not stage it to be added, and remove it from the
     * staging area if it is already there (as can happen when a file is changed,
     * added, and then changed back to it’s original version). The file will no longer
     * be staged for removal (see gitlet rm), if it was at the time of the command. */
    public static void add(String filename) {
        File file = Utils.join(CWD, filename);
        if (!file.exists()) {
            Main.exitMessage("File does not exist.");
        }
        // Load staging area.
        Index index = Index.load();

        /* Create blob from file. */
        Blob blob = new Blob(Utils.readContentsAsString(file));
        // Load current commit in order to check for changes.
        Commit commit = Commit.load(getCurrentHead());
        HashMap<String, String> currentBlobs = commit.getBlobs();
        // Check whether file is unchanged since current commit.
        if (currentBlobs.containsKey(blob.getID())) {
            // Remove from staging area if there.
            if (index.getAdditions().containsKey(filename)) {
                index.getAdditions().remove(filename);
            }
        }
        // Stage file
        index.stage(filename, blob.getID());
        index.save();
    }

    /** Commit command.
     * Saves a snapshot of tracked files in the current commit and staging area
     * so they can be restored at a later time, creating a new commit.
     * The Commit constructor method handles the logic. */
    public static void commit(String message) {
        // Retrieve the currently checked out branch and its head commit.
        String parentID = getCurrentHead();

        // Check for changes to be saved.
        Index index = Index.load();
        if (index.getAdditions().isEmpty()) {
            Main.exitMessage("No changes added to the commit.");
        }

        // Create a new commit object and save it.
        Commit commit = new Commit(parentID, null, message);
        commit.save();

        // Clear staging area and save it.
        index.clear();
        index.save();
    }

    /** Returns the name of the currently checked out branch. */
    public static String getCurrentBranch() {
        return Utils.readContentsAsString(HEAD);
    }

    /** Records the name of the currently checked out branch. */
    public static void updateHead(String branchName) {
        Utils.writeContents(HEAD, branchName);
    }

    /** Returns the commit ID of the current head. */
    public static String getCurrentHead() {
        String currentBranch = getCurrentBranch();
        return Utils.readContentsAsString(Utils.join(BRANCHES, currentBranch));
    }

    /** Overwrites the named branch file with the newest commit ID. */
    public static void updateBranchHead(String branchName, String commitID) {
        File branch = Utils.join(BRANCHES, branchName);
        Utils.writeContents(branch, commitID);
    }
}
