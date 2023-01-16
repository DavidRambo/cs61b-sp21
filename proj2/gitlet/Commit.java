package gitlet;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Date;
import java.io.Serializable;

/** Represents a gitlet commit object.
 * The snapshot of the working directory is recorded as a HashMap. Its key is a
 * String of the filename, and its value is a String of the corresponding blob's
 * filename.
 *
 *  @author David Rambo
 */
public class Commit implements Serializable {
    /** Map of blobs: <filename, blobID> */
    private HashMap<String, String> blobs;
    /** This commit's hash ID. */
    private final String commitID;
    /** Parent commit ID. This is the default one for commits. */
    private final String firstParentID;
    /** Second parent commit, in the event of a merge. */
    private final String secondParentID;
    /** The message of this Commit. */
    private final String message;
    /** Date of the commit. */
    private final Date timestamp;

    /** Constructor method for the initial (empty) commit. */
    public Commit() {
        blobs = new HashMap<String, String>();
        firstParentID = null;
        secondParentID = null;
        message = "initial commit";
        timestamp = new Date(0);
        commitID = calcHash();
    }

    /** Constructor for Commit objects.
     * By default, a commit has the same file contents as its parent. Files
     * staged for addition and removal are the updates to the commit.
     * @param message Message describing the commit's changes.
     * @param firstParent ID of the preceding commit.
     * @param secondParent ID of a second commit when merging.
     * */
    public Commit(String firstParent, String secondParent, String message) {
        this.firstParentID = firstParent;
        this.secondParentID = secondParent;
        this.message = message;
        this.blobs = new HashMap<>();
        this.timestamp = new Date();
        this.commitID = calcHash();

        /* Clone parent's blobs. */
        if (firstParentID != null) {
            this.blobs = getBlobs(firstParentID);
        }

        /* Commit files in staging area. */
        Index index = Index.load();
        blobs.putAll(index.getAdditions());
    }

    /** Calculates the sha-1 hash from the commit's message and timestamp.
     * This serves as its ID and also filename. */
    private String calcHash() {
        return Utils.sha1(message, timestamp.toString());
    }

    /** Save the commit object to the file system. */
    public void save() {
        File file = Utils.join(Repository.COMMITS_DIR, this.commitID);
        Utils.writeObject(file, this);
    }

    /** Loads a commit object from the file system. */
    public static Commit load(String commitID) {
        File file = Utils.join(Repository.COMMITS_DIR, commitID);
        return Utils.readObject(file, Commit.class);
    }

    /** Returns the HashMap of blobs belonging to the commit with the provided
     * commit ID. */
    public static HashMap<String, String> getBlobs(String commitID) {
        File file = Utils.join(Repository.COMMITS_DIR, commitID);
        Commit commit = Utils.readObject(file, Commit.class);
        return commit.getBlobs();
    }

    /** Returns blobs HashMap. */
    public HashMap<String, String> getBlobs() {
        return this.blobs;
    }
    
    public String getID() {
        return this.commitID;
    }

    public String getParentID() {
        return this.firstParentID;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public String getMessage() {
        return this.message;
    }

    public String toString() {
        String spacer = "===\n";
        String name = String.format("commit %s", this.getID());
        DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");
        String time = df.format(getTimestamp());
        String date = "\nDate: " + time + "\n";
        return spacer + name + date + this.getMessage();
    }
}
