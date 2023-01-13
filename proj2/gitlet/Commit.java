package gitlet;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;

/** Represents a gitlet commit object.
 * The empty constructor creates the initial commit when initializing
 * (see Repository.init()).
 * *
 *  @author David Rambo
 */
public class Commit implements Serializable {

    /* The message of this commit. */
    private final String message;
    /* Time that the commit was made. */
    private final Date timestamp;
    /* Previous commits relative to this one. */
    private final String parentOne;  // Default parent commit's id.
    private final String parentTwo;  // In the event of a merge, this will be non-null.
    private final String commitID;  // This commit's hash code.
    /* It needs to be able to tell when two blobs are different versions of the
     * same file. To do so, I'll use a map that associates a file name with its
     * SHA-1 hash value (i.e. its blobName). */
    private HashMap<String, String> blobs;

    /* Directory that stores Commit objects. */
    static final File COMMITS_DIR = Utils.join(Repository.GITLET_DIR, "/commits");

    // TODO: Generate log message.

    /** Constructs the initial (empty) commit. */
    public Commit() {
        this.message = "initial commit";
        this.parentOne = null;
        this.parentTwo = null;
        this.timestamp = new Date(0);
        this.commitID = calcHash();
        this.blobs = new HashMap<String, String>();
    }

    /** Constructor method for commits.
     * @param message : commit message.
     * @param parentOne : SHA-1 hash code id to first parent commit.
     * @param parentTwo : SHA-1 hash code id to second parent commit if merging. */
    public Commit(String message, String parentOne, String parentTwo) {
        this.message = message;
        this.parentOne = parentOne;
        this.parentTwo = parentTwo;
        this.timestamp = new Date();
        this.commitID = calcHash();
        this.blobs = new HashMap<>();

        // Load the staging area
        Index index = Index.load();
        // Ensure there are changes to be committed.
        if (index.getAdditions().isEmpty()) {
            Repository.exitMsg("No changes added to the commit.");
        }

        // Get the HashMap of those file names and their corresponding blobs.
        HashMap<String, String> stagedFiles = index.getAdditions();
        // Get the HashMap of the filenames staged for removal.
        HashSet<String> stagedRemovals = index.getRemovals();

        // Clone the HEAD commit
        Commit headCommit = loadCommit(parentOne);
        this.blobs.putAll(headCommit.blobs);

        /* Map those blobs against their hash names and store, while also
         * writing a copy of them into the "/blobs/" subdirectory.
         * entry.getKey() is the filename; entry.getValue() is the blob. */
        for (Map.Entry<String, String> entry : stagedFiles.entrySet()) {
            this.blobs.put(entry.getKey(), entry.getValue());
            File blobFile = Utils.join(Blob.BLOBS_DIR, entry.getValue());
            Utils.writeObject(blobFile, entry.getValue());
        }

        // Remove files.
        for (String name : stagedRemovals) {
            // Remove from the commit's map of blobs.
            blobs.remove(name);

            // Remove from the working directory.
            Utils.join(Repository.CWD, name).delete();
        }

        // Clear the Index of its staged files.
        index.clear();
        // And save it.
        index.save();

        // Update branch's head to point to this commit.
        Branch.updateCommit(HEAD.getCurrentHead(), commitID);
    }

    public String getMessage() { return this.message; }

    public String getTimestamp() { return this.timestamp.toString(); }

    public String getCommitID() {
        return this.commitID;
    }

    /** Calculates the hash of the commit using the commit's message
     * and its timestamp.
     * */
    private String calcHash() {
        // Create byte array out of this Commit object, which will be passed to
        // the Utils.sha1() method.
        byte[] data = Utils.serialize(this);

        return Utils.sha1(data);
    }

    /** Saves the Commit object to the file system as ".gitlet/commits/[commitID]". */
    public void save() {
        File commitFile = Utils.join(COMMITS_DIR, this.getCommitID());
        Utils.writeObject(commitFile, this);
    }

    /** Returns a Commit object with the @param commitID as its filename.
     * Recall that this is determined by the Commit.calcHash() method. */
    public static Commit loadCommit(String commitID) {
        // TODO: Handle short IDs (i.e. commitID < 40 characters)
        File commitFile = Utils.join(COMMITS_DIR, commitID);
        if (!commitFile.exists()) {
            Repository.exitMsg("No commit with that id exists.");
        }
        return Utils.readObject(commitFile, Commit.class);
    }

    /** Returns a Commit object's HashMap of blobs. */
    public HashMap<String, String> getBlobs() {
        return blobs;
    }

    /** Returns the SHA-1 hash code of the blob corresponding to the @param fileName. */
    public String getBlobName(String fileName) {
        return blobs.get(fileName);
    }

    /** Takes the name of a file and returns the contents of that file as a String. */
    public String getCommittedFileContents(String fileName) {
        return Blob.readBlobAsString(getBlobName(fileName));
    }
}
