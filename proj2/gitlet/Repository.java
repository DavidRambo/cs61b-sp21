package gitlet;

import java.io.File;
import java.util.*;

import static gitlet.Utils.*;

/** Represents a gitlet repository.
 *
 *  @author David Rambo
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
    /** The directory that stores branch names and their heads. */
    public static final File BRANCHES = join(GITLET_DIR, "/refs");
    /** The staging area file. */
    public static final File INDEX = join(GITLET_DIR, "index");
    /** The file that stores the currently checked out branch name. */
    public static final File HEAD = join(GITLET_DIR, "HEAD");

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
     * added, and then changed back to its original version). The file will no longer
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
        // Check whether file is unchanged since current commit.
        if (commit.getBlobs().containsKey(filename)) {
            if (commit.getBlobs().get(filename).equals(blob.getID())) {
                // Remove from staging area if unchanged.
                index.getAdditions().remove(filename);
                // If staged for removal, remove from removals list.
                index.getRemovals().remove(filename);
                index.save();
                return;
            }
        }

        // Stage file
        index.stage(filename, blob.getID());

        // If staged for removal, remove from removals list.
        index.getRemovals().remove(filename);
        index.save();
        // Save blob
        File blobFile = Utils.join(BLOBS_DIR, blob.getID());
        Utils.writeObject(blobFile, blob);
    }

    /** Unstage the file if it is currently staged for addition. If the file is
     * tracked in the current commit, stage it for removal and remove the file from
     * the working directory if the user has not already done so. (Does not remove
     * if it is untracked in the current commit.) */
    public static void rmCommand(String filename) {
        // Load the staging area.
        Index index = Index.load();
        // Load head commit.
        Commit headCommit = Commit.load(getCurrentHead());

        if (index.isStaged(filename) || headCommit.getBlobs().containsKey(filename)) {
            index.remove(filename); // Handles whether file should be deleted.
        } else {
            Main.exitMessage("No reason to remove the file.");
        }
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
        if (index.getAdditions().isEmpty() && index.getRemovals().isEmpty()) {
            Main.exitMessage("No changes added to the commit.");
        }

        // Create a new commit object and save it.
        Commit commit = new Commit(parentID, null, message);
        commit.save();

        // Clear staging area and save it.
        index.clear();
        index.save();

        // Update branch with new commit ID
        updateBranchHead(getCurrentBranch(), commit.getID());
    }

    /* *** Checkout Commands ***
     * There are three outcomes:
     * 1. A file is checked out from the HEAD.
     * 2. A file is checked out from a specified commit.-sp21
     * 3. An entire branch is checked out.
     * If a branch is being checked out, then it updates the HEAD file and
     * loads the checked out branch's head commit's files from its blobs.
     * */

    /** Checkout a single file from the HEAD commit.
     * java gitlet.Main checkout -- [file name] */
    public static void checkoutFile(String filename) {
        // Get commit ID of the current HEAD.
        String commitID = getCurrentHead();
        // Call method to checkout the file.
        checkoutFile(commitID, filename);
    }

    /** Checkout a single file from a specified commit.
     * java gitlet.Main checkout [commit id] -- [file name] */
    public static void checkoutFile(String commitID, String filename) {
        // Try to find a matching ID for abbreviated IDs of fewer than 40 characters
        if (commitID.length() < 40) {
            commitID = matchCommitID(commitID);
        }
        // Ensure a commit with that ID exists.
        File commitFile = Utils.join(COMMITS_DIR, commitID);
        if (!commitFile.exists()) {
            Main.exitMessage("No commit with that id exists.");
        }
        // Load the Commit object.
        Commit commit = Commit.load(commitID);
        // Ensure file exists in that commit.
        if (!commit.getBlobs().containsKey(filename)) {
            Main.exitMessage("File does not exist in that commit.");
        }
        // Get the ID of the blob from the checked out commit.
        String blobID = commit.getBlobs().get(filename);
        // Load the blob.
        File blobFile = Utils.join(BLOBS_DIR, blobID);
        Blob blob = Utils.readObject(blobFile, Blob.class);
        // Write to CWD
        File checkoutFile = Utils.join(CWD, filename);
        Utils.writeContents(checkoutFile, blob.getContents());
    }

    /** Searches through the commit object files for the best match against the given
     * abbreviated ID. It compares the abbreviated ID's characters against each of those
     * files, removing failed matches. If one remains, it returns; otherwise it exits
     * with a message.
     * <p>
     * Since Gitlet does not implement a tree structure by which to organize its commit
     * objects, this is slower relative to how Git does it. The Utils.plainFilenamesIn()
     * method does provide a sorted List of Strings, so one optimization would stop
     * checking for a match once the range of Strings exceeds the abbreviated ID.
     *
     * @param shortID abbreviated ID for the commit
     * @return full commit ID
     */
    private static String matchCommitID(String shortID) {
        List<String> allCommits = Utils.plainFilenamesIn(COMMITS_DIR);
        assert allCommits != null;
        List<String> matchingCommits = new ArrayList<>();

        for (String fullID : allCommits) {
            if (fullID.regionMatches(true, 0, shortID, 0, shortID.length())) {
                matchingCommits.add(fullID);
            }
        }

        // More than one match, then exit.
        if (matchingCommits.size() != 1) {
            Main.exitMessage("No commit with that ID exists.");
        }

        return matchingCommits.get(0);
    }

    /** Checkout an entire branch.
     * > java gitlet.Main checkout [branch name]
     * Takes all files at the head of the specified branch and puts them into
     * the working directory, overwriting versions of those files already there.
     * Sets the checked out branch to be the HEAD. Any files tracked in current
     * branch but not by the checked out branch are deleted. Note that this differs
     * from Git, which would block a checkout command that would overwrite files
     * that have been modified or staged for addition.
     * Staging area is cleared.*/
    public static void checkoutBranch(String branchName) {
        // Ensure branch exists.
        List<String> branches = Utils.plainFilenamesIn(BRANCHES);
        assert branches != null;
        if (!branches.contains(branchName)) {
            Main.exitMessage("No such branch exists.");
        }
        // Ensure not already checked out.
        if (branchName.equals(getCurrentBranch())) {
            Main.exitMessage("No need to check out the current branch.");
        }
        // Ensure no untracked files would be overwritten.
        if (!untrackedFiles().isEmpty()) {
            Main.exitMessage("There is an untracked file in the way; delete it, "
                    + "or add and commit it first.");
        }
        checkoutCommit(getBranchHead(branchName));

        // Update HEAD to point at checked out branch
        updateHead(branchName);

        // Load and clear the staging area.
        Index index = Index.load();
        index.clear();
        index.save();
    }

    /** Loads all files tracked by a given commit and deletes files that were tracked
     * by preceding head but which it does not track. */
    private static void checkoutCommit(String commitID) {
        // Load the commit to be checked out
        Commit checkoutCommit = Commit.load(commitID);

        // Load every file to working directory
        for (String filename : checkoutCommit.getBlobs().keySet()) {
            String content = getBlobContent(commitID, filename);
            File checkoutFile = Utils.join(CWD, filename);
            Utils.writeContents(checkoutFile, content);
        }

        /* Delete working files that are not tracked by the given commit. */
        List<String> plainFiles = Utils.plainFilenamesIn(CWD);
        assert plainFiles != null;
        for (String filename : plainFiles) {
            if (!checkoutCommit.getBlobs().containsKey(filename)) {
                File file = Utils.join(CWD, filename);
                file.delete();
            }
        }
    }

    /** Prints a log starting with the head commit and proceeding back through
     * parent commits to the initial commit. */
    public static void log() {
        StringBuilder output = new StringBuilder();
        String commitID = getCurrentHead();
        while (commitID != null) {
            Commit commit = Commit.load(commitID);
            output.append(commit.toString()).append("\n");
            commitID = commit.getParentID();
        }
        System.out.println(output);
    }

    /** Like log, except displays information about all commits ever made. The order
     * does not matter. */
    public static void globalLog() {
        StringBuilder output = new StringBuilder();
        List<String> allCommits = Utils.plainFilenamesIn(COMMITS_DIR);
        for (String commitID : allCommits) {
            Commit commit = Commit.load(commitID);
            output.append(commit.toString()).append("\n");
        }
        System.out.println(output);
    }

    /** Creates a new branch with the name provided.
     * It sets the new branch's head to the current HEAD.
     * It does not checkout the new branch. */
    public static void branch(String name) {
        // Retrieve names of extant branches
        List<String> branches = Utils.plainFilenamesIn(BRANCHES);
        assert branches != null;
        if (branches.contains(name)) {
            Main.exitMessage("A branch with that name already exists.");
        }

        File newBranch = Utils.join(BRANCHES, name);
        Utils.writeContents(newBranch, getCurrentHead());
    }

    /** Deletes the branch with the given name. This removes only the pointer to that branch.
     * Its associated commits are untouched.
     */
    public static void rmBranch(String branchName) {
        if (getCurrentBranch().equals(branchName)) {
            Main.exitMessage("Cannot remove the current branch.");
        }
//        List<String> branches = Utils.plainFilenamesIn(BRANCHES);
//        assert branches != null;
//        if (!branches.contains(branchName))
//            Main.exitMessage("A branch with that name does not exist.");
        File file = Utils.join(BRANCHES, branchName);

        if (!file.delete()) {
            Main.exitMessage("A branch with that name does not exist.");
        }
    }

    /** Prints out:
     * - what branches currently exist, marking the current with an asterisk
     * - files staged for addition
     * - files staged for removal
     *   ::: Extra Credit :::
     * - modifications not staged for commit
     * - untracked files*/
    public static void status() {
        StringBuilder output = new StringBuilder();

        /* Create list of branches, adding an asterisk to the currently checked out branch. */
        output.append("=== Branches ===\n");
        List<String> branches = Utils.plainFilenamesIn(BRANCHES);
        String headBranch = getCurrentBranch();
        assert branches != null;
        for (String branchName : branches) {
            if (headBranch.equals(branchName)) {
                output.append("*").append(branchName).append("\n");
            } else {
                output.append(branchName).append("\n");
            }
        }

        Index index = Index.load();
        output.append("\n=== Staged Files ===\n");
        for (String filename : index.getAdditions().keySet()) {
            output.append(filename).append("\n");
        }

        output.append("\n=== Removed Files ===\n");
        for (String filename : index.getRemovals()) {
            output.append(filename).append("\n");
        }

        /* The next two sections are extra credit. */
        output.append("\n=== Modifications Not Staged For Commit ===\n");
        for (String filename : modifiedFiles()) {
            output.append(filename).append("\n");
        }

        output.append("\n=== Untracked Files ===\n");
        for (String filename : untrackedFiles()) {
            output.append(filename).append("\n");
        }

        System.out.println(output);
    }

    /** Prints out the ids of all commits that have the given commit message, one per line.
     * @param message String of the message in the sought for commit(s).
     */
    public static void find(String message) {
        List<String> allCommits = Utils.plainFilenamesIn(COMMITS_DIR);
        StringBuilder output = new StringBuilder();

        for (String commitID : allCommits) {
            Commit commit = Commit.load(commitID);
            if (commit.getMessage().equals(message)) {
                output.append(commitID).append("\n");
            }
        }

        if (output.isEmpty()) {
            Main.exitMessage("Found no commit with that message.");
        }
        System.out.println(output);
    }

    /** Checks out all the files tracked by the given commit. Removes tracked files that
     * are not present in that commit. Also moves the current branch's head to that commit
     * node. The commit ID may be abbreviated. Staging area is cleared. Basically, reset
     * is checkout of an arbitrary commit that also changes the current branch head.
     *
     * @param commitID String of the given commit's ID
     */
    public static void reset(String commitID) {
        /* Check for untracked files in the way. */
        if (!untrackedFiles().isEmpty()) {
            Main.exitMessage("There is an untracked file in the way; delete it, "
                    + "or add and commit it first.");
        }
        // Try to find a matching ID for abbreviated IDs of fewer than 40 characters
        if (commitID.length() < 40) {
            commitID = matchCommitID(commitID);
        }

        checkoutCommit(commitID);

        // Load and clear the staging area.
        Index index = Index.load();
        index.clear();
        index.save();

        // Set head commit.
        updateBranchHead(getCurrentBranch(), commitID);
    }

    /** Merges the current branch with the specified "given" branch.
     * It treats the current branch head as the parent commit, and then by comparing
     * files between the split point commit, the HEAD commit, and the head of the given
     * branch, it stages files for addition or removal before creating a new commit.
     */
    public static void merge(String givenBranch) {
        validateMerge(givenBranch);
        /* Load the two commits. */
        Commit headCommit = Commit.load(getCurrentHead());
        String givenID = getBranchHead(givenBranch);
        Commit givenCommit = Commit.load(givenID);
        /* Find the common ancestor. */
        LinkedList<String> currentHistory = Commit.getHistory(headCommit.getID());
        LinkedList<String> givenHistory = Commit.getHistory(givenCommit.getID());
        validateHistory(givenBranch, currentHistory, givenHistory);
        // Determine split point and load that commit.
        String splitID = Commit.findSplit(currentHistory, givenHistory);
        Commit splitCommit = Commit.load(splitID);

        LinkedList<String> conflicts = new LinkedList<>();
        Index index = Index.load();

        // Go through each file in the current branch's HEAD commit.
        for (String filename : headCommit.getBlobs().keySet()) {
            // Load the blob ID for the file
            String headBlob = headCommit.getBlobs().get(filename);
            // Check for file in given branch and load blobID of its version
            if (givenCommit.getBlobs().containsKey(filename)) {
                String givenBlob = givenCommit.getBlobs().get(filename);
                // Check for file at split point
                if (splitCommit.getBlobs().containsKey(filename)) {
                    String splitBlob = splitCommit.getBlobs().get(filename);

                    // Modified in given branch...
                    if (!splitBlob.equals(givenBlob)) {
                        // but not modified in HEAD -> keep given version
                        if (splitBlob.equals(headBlob)) {
                            index.stage(filename, givenBlob);
                        // and is modified in HEAD -> check for conflict
                        } else if (!givenBlob.equals(headBlob)) {
                            conflicts.add(filename);
                        }
                    }
                // Not in split commit means modified in both.
                } else if (!headBlob.equals(givenBlob)) {
                    // If different, then in conflict.
                    conflicts.add(filename);
                } // Otherwise, do nothing in order to keep the HEAD version.
            } else if (splitCommit.getBlobs().containsKey(filename)) { // Not in given branch.
                String splitBlob = splitCommit.getBlobs().get(filename);
                // If unmodified in HEAD since split, then remove.
                if (headBlob.equals(splitBlob)) {
                    index.remove(filename);
                }
            } // Otherwise, unique to HEAD, so do nothing to keep it.
        }
        // Go through files in given branch that are NOT in current branch's HEAD commit.
        for (String filename : givenCommit.getBlobs().keySet()) {
            String givenBlobID = givenCommit.getBlobs().get(filename);
            if (!headCommit.getBlobs().containsKey(filename)) {
                // Not in split (nor in HEAD), then stage given branch's version.
                if (!splitCommit.getBlobs().containsKey(filename)) {
                    checkoutFile(givenID, filename);
                    index.stage(filename, givenBlobID);
                // Else, since it is in split, if modified then stage given branch's version.
                } else if (!givenBlobID.equals(splitCommit.getBlobs().get(filename))) {
                    index.stage(filename, givenBlobID);
                } /* Otherwise, it is unmodified in given branch since split,
                     but not present in HEAD, so it remains removed. */
            }
        }
        index.save();
        if (!conflicts.isEmpty()) {
            mergeConflict(conflicts, givenBranch);
        } else { // No conflicts. Conclude merge and print message.
            Commit mergeCommit = new Commit(headCommit.getID(), givenID, "Merged " + givenBranch
                                            + " into " + getCurrentBranch() + ".");
            mergeCommit.save();
            updateBranchHead(getCurrentBranch(), mergeCommit.getID());
        }
    }

    /** Helper method to handle a merge commit with conflicts.
     * Handle merge conflicts by concatenating the two versions and staging the file
     * for addition. Whereas the above logic handles already existing blobs, and therefore
     * stages directly with the index, here a new blob needs to be created, so the
     * Repository.add() method is used. Then conclude the merge and print out merge
     * conflict message. */
    private static void mergeConflict(LinkedList<String> conflicts, String givenBranch) {
        Commit headCommit = Commit.load(getCurrentHead());
        Commit givenCommit = Commit.load(getBranchHead(givenBranch));

        for (String filename : conflicts) {
            StringBuilder conflictFile = new StringBuilder();
            conflictFile.append("<<<<<<< HEAD\n");
            // Load the current HEAD blob.
            File headBlobFile = Utils.join(BLOBS_DIR, headCommit.getBlobs().get(filename));
            Blob headBlob = Utils.readObject(headBlobFile, Blob.class);
            conflictFile.append(headBlob.getContents());
            conflictFile.append("=======\n");
            // Load the given blob.
            File givenBlobFile = Utils.join(BLOBS_DIR, givenCommit.getBlobs().get(filename));
            Blob givenBlob = Utils.readObject(givenBlobFile, Blob.class);
            conflictFile.append(givenBlob.getContents());
            conflictFile.append(">>>>>>>\n");
            File file = Utils.join(CWD, filename);
            Utils.writeContents(file, conflictFile.toString());
            add(filename); // Stage merge-conflicted file for commit.
        }

        String mergeMsg = "Merged "
                + getCurrentBranch()
                + " into "
                + givenBranch
                + ".";
        Commit mergeCommit = new Commit(headCommit.getID(), getBranchHead(givenBranch), mergeMsg);
        mergeCommit.save();
        updateBranchHead(getCurrentBranch(), mergeCommit.getID());
        System.out.println("Encountered a merge conflict");
    }

    private static void validateHistory(String givenBranch, LinkedList<String> currentHistory,
                                        LinkedList<String> givenHistory) {
        // Check whether given branch is unmodified ancestor of current branch.
        if (currentHistory.contains(getBranchHead(givenBranch))) {
            Main.exitMessage("Given branch is an ancestor of the current branch.");
        }
        // Check whether current branch can be fast-forwarded.
        if (givenHistory.contains(getCurrentHead())) {
            checkoutBranch(givenBranch);
            Main.exitMessage("Current branch is fast-forwarded.");
        }
    }

    /** Error checking for merge method. */
    private static void validateMerge(String branchName) {
        /* Check for untracked files in the way. */
        if (!untrackedFiles().isEmpty()) {
            Main.exitMessage("There is an untracked file in the way; delete it, "
                    + "or add and commit it first.");
        }
        /* Check whether staging area is clear. */
        Index index = Index.load();
        if (!index.getAdditions().isEmpty() || !index.getRemovals().isEmpty()) {
            Main.exitMessage("You have uncommited changes.");
        }

        // Ensure not already checked out.
        if (branchName.equals(getCurrentBranch())) {
            Main.exitMessage("Cannot merge a branch with itself.");
        }

        /* Check that merge is possible with specified branch. */
        List<String> branches = Utils.plainFilenamesIn(BRANCHES);
        assert branches != null;
        if (!branches.contains(branchName)) {
            Main.exitMessage("A branch with that name does not exist.");
        }
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

    /** Returns the specified branch's head commit ID. */
    public static String getBranchHead(String branchName) {
        File branch = Utils.join(BRANCHES, branchName);
        return Utils.readContentsAsString(branch);
    }

    /** Overwrites the named branch file with the newest commit ID. */
    public static void updateBranchHead(String branchName, String commitID) {
        File branch = Utils.join(BRANCHES, branchName);
        Utils.writeContents(branch, commitID);
    }

    /** Returns a list of untracked files in the working directory. */
    public static LinkedList<String> untrackedFiles() {
        // LinkedList to hold untracked files.
        LinkedList<String> files = new LinkedList<>();

        /* Fill it with the names of files in working directory that are
        neither referenced by the current commit nor in the staging area. */
        Commit headCommit = Commit.load(getCurrentHead());
        Index index = Index.load();
        for (String filename : Objects.requireNonNull(plainFilenamesIn(CWD))) {
            boolean tracked = headCommit.getBlobs().containsKey(filename);
            boolean stagedForAdd = index.getAdditions().containsKey(filename);
            boolean stagedForRm = index.getRemovals().contains(filename);
            if (!tracked && !stagedForAdd && !stagedForRm) {
                files.add(filename);
            }
        }
        Collections.sort(files);
        return files;
    }

    /** Returns a list of files that have been modified but not staged, including a parenthetical
     * indication of whether the file has been modified or deleted.
     * Such a file is either:
     * - tracked in current commit, changed in working directory, but not staged;
     * - staged for addition, but with different contents than in the working directory;
     * - staged for addition, but deleted in the working directory;
     * - not staged for removal, but tracked in the current commit and deleted from the
     *   working directory.
     *
     *   TODO: Does not catch untracked files staged for addition and further modified.
     * */
    public static LinkedList<String> modifiedFiles() {
        LinkedList<String> files = new LinkedList<>();

        Commit headCommit = Commit.load(getCurrentHead());
        Index index = Index.load();

        /* Check tracked files. */
        for (String filename : headCommit.getBlobs().keySet()) {
            File workingFile = Utils.join(CWD, filename);

            // File was removed from working directory and not staged for removal.
            if (!workingFile.exists() && !index.getRemovals().contains(filename)) {
                files.add(filename + " (deleted)");
                continue;
            }

            if (workingFile.exists()) {
                String currentContent = Utils.readContentsAsString(workingFile);
                if (index.getAdditions().containsKey(filename)) {
                    String stagedContent = index.getContent(filename);
                    if (!currentContent.equals(stagedContent)) {
                        // Was staged, but has since been changed.
                        files.add(filename + " (modified)");
                        continue;
                    }
                } else {
                    String trackedContent = getBlobContent(getCurrentHead(), filename);
                    if (!currentContent.equals(trackedContent)) {
                        // Not staged and changed since last commit.
                        files.add(filename + " (modified)");
                        continue;
                    }
                }
            }
        }

        /* Check files that are not yet tracked, but staged for addition.
        * If deleted from working directory, then mark down. */
        for (String filename : index.getAdditions().keySet()) {
            if (!headCommit.getBlobs().containsKey(filename)) {
                File file = Utils.join(CWD, filename);
                if (!file.exists()) {
                    files.add(filename + " (deleted)");
                }
            }
        }

        Collections.sort(files);
        return files;
    }

    /** Returns the contents of a blob.
     * @param commitID ID of the commit that references the blob
     * @param filename name of the file of which the blob stores a snapshot */
    public static String getBlobContent(String commitID, String filename) {
        Commit commit = Commit.load(commitID);
        File blobFile = Utils.join(BLOBS_DIR, commit.getBlobs().get(filename));
        if (!blobFile.exists()) {
            Main.exitMessage("No blob exists for" + filename + " in commit " + commitID);
        }
        Blob blob = Utils.readObject(blobFile, Blob.class);
        return blob.getContents();
    }

    /** Checks whether the file exists in the working directory. */
    public static boolean fileExists(String filename) {
        File file = Utils.join(CWD, filename);
        return file.exists();
    }

}
