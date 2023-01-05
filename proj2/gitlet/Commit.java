package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.io.File;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author David Rambo
 */
public class Commit implements Serializable {

    /* The message of this commit. */
    private final String message;
    /* Time that the commit was made. */
    private final Date timestamp;
    /* Previous commits relative to this one. */
    private final Commit parentOne;  // Default parent commit.
    private final Commit parentTwo;  // In the event of a merge, this will be non-null.
    private final String commitID;  // This commit's hash code.
    /* It needs to be able to tell when two blobs are different versions of the
     * same file. To do so, I'll use a map that associates a file name with its
     * SHA-1 hash value (i.e. its blob). */
    private HashMap<String, String> blobs;

    /* Directory that stores Commit objects. */
    static final File COMMITS_DIR = Utils.join(Repository.GITLET_DIR, "/commits");

    // TODO: Generate log message.

    /* Constructor for initial (empty) commit. */
    public Commit() {
        this.message = "initial commit";
        this.parentOne = null;
        this.parentTwo = null;
        this.timestamp = new Date(0);
        this.commitID = calcHash();
        blobs = new HashMap<String, String>();
    }

    /* Constructor method for commits. */
    public Commit(String message, Commit parentOne, Commit parentTwo) {
        this.message = message;
        this.parentOne = parentOne;
        this.parentTwo = parentTwo;
        this.timestamp = new Date();
        commitID = calcHash();
        blobs = new HashMap<String, String>();
    }

    /** TODO:
     * */
    private String calcHash() {
        return null;
    }

    public String getMessage() { return this.message; }

    public String getTimestamp() { return this.timestamp.toString(); }

    public Commit getParentOne() { return this.parentOne; }

    public Commit getParentTwo() { return this.parentTwo; }

    public String getCommitID() {
        return this.commitID;
    }

    public void writeCommit() {
        File commitFile = Utils.join(COMMITS_DIR, this.getCommitID());
        Utils.writeObject(commitFile, this);
    }
}
