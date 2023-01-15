package gitlet;

// TODO: any imports you need here
import java.util.HashMap;
import java.util.Date;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit {
    /**
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */
    /** Map of blobs: <filename, blobID> */
    HashMap<String, String> blobs;
    /** This commit's hash ID. */
    String commitID;
    /** Parent commit ID. This is the default one for commits. */
    String firstParentID;
    /** Second parent commit, in the event of a merge. */
    String secondParentID;
    /** The message of this Commit. */
    private String message;
    /** Date of the commit. */
    private Date timestamp;

    /* TODO: fill in the rest of this class. */

    /** Constructor method for the initial (empty) commit. */
    public Commit() {
        blobs = new HashMap<>();
        firstParentID = null;
        secondParentID = null;
        message = "initial commit";
        timestamp = new Date(0);
        commitID = calcHash();
    }

    /** Constructor for Commit objects. 
     * @param message Message describing the commit's changes.
     * @param firstParentID ID of the preceding commit.
     * @param secondParentID ID of a second commit when merging.
     * */
    public Commit(String firstParent, String secondParent, String message) {
        this.firstParentID = firstParent;
        this.secondParentID = secondParent;
        this.message = message;
        this.blobs = new HashMap<>();
        this.timestamp = new Date();
        this.commitID = calcHash();

        // Clone parent commit's blobs.
        blobs.putAll(getBlobs(firstParentID));
    }

    /** Calculates the sha-1 hash from the commit's message and timestamp.
     * This serves as its ID and also filename. */
    private String calcHash() {
        return Utils.sha1(message, timestamp.toString());
    }

    /** Save the commit object to the file system. */
    public void save() {
        File commitFile = Utils.join(Repository.COMMITS_DIR, this.commitID);
        Utils.writeObject(commitFile, this);
    }

    /** Returns the HashMap of blobs belonging to the commit with the provided
     * commit ID. */
    public HashMap<String, String> getBlobs(String commitID) {
        Commit commit = Utils.readObject(COMMITS_DIR, commitID);
        return commit.blobs;
    }
}
