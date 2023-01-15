package gitlet;

import java.io.File;
import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
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
    /** The directory in which to store blobs. */
    public static final File BLOBS_DIR = join(GITLET_DIR, "/blobs");
    /** The directory in which to store commits. */
    public static final File COMMITS_DIR = join(GITLET_DIR, "/commits");
    /** The file that stores branch names and their heads. */
    public static final File BRANCHES = join(GITLET_DIR, "/refs");
    /** The staging area file. */
    public static final File INDEX = join(GITLET_DIR, "index");
    /** The file that stores the currently checked out commit. */
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
    public static void initCommand() {
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

        /* Create the HEAD file and write "master" to it. */
        Utils.writeContents(HEAD, "master");
        
        /* Create the file for the master branch and enter initial commit's ID. */
        String currentBranch = getCurrentBranch();
        File branch = Utils.join(BRANCHES, currentBranch);
        Utils.writeContents(branch, commit.getID());
    }

    /** Adds file to the staging area. */
    public static void add(String filename) {
        // Handle 
        // Load staging area.
    }

    /** Commit command. */
    public static void commit(String message) {
        // Retrieve the currently checked out branch and its head commit.
        String parentID = getCurrentHead();
        // Create a fresh commit object.
        Commit commit = new Commit(parentID, null, message);
        // Clone parent commit's blobs.
        blobs.putAll(getBlobs(firstParentID));
    }

    /** Returns the name of the currently checked out branch. */
    public static String getCurrentBranch() {
        return Util.readContentsAsString(HEAD);
    }

    /** Returns the commit ID of the current head. */
    public static String getCurrentHead() {
        String currentBranch = getCurrentBranch();
        return Utils.readContentsAsString(Utils.join(BRANCHES, currentBranch));
    }

    /** Overwrites the named branch file with the newest commit ID. */
    public static void updateBranchHead(String branchName, String commitID) {
        throw new UnsupportedArgumentException();
    }
}
