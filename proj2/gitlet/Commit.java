package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
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
     * SHA-1 hash value (i.e. its blob). */
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
        blobs = new HashMap<String, String>();
        writeCommit();
    }

    /** Constructor method for commits.
     * @param message : commit message.
     * @param parentOne : SHA-1 hash code id to first parent commit.
     * @param parentTwo : SHA-1 hash code id to second parent commit. */
    public Commit(String message, String parentOne, String parentTwo) {
        this.message = message;
        this.parentOne = parentOne;
        this.parentTwo = parentTwo;
        this.timestamp = new Date();

        blobs = new HashMap<String, String>();
        // TODO: Add blobs to HashMap from staging area.

        commitID = calcHash();

        writeCommit();
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

    public String getMessage() { return this.message; }

    public String getTimestamp() { return this.timestamp.toString(); }

    // public Commit getParentOne() { return this.parentOne; }

    // public Commit getParentTwo() { return this.parentTwo; }

    public String getCommitID() {
        return this.commitID;
    }

    public void writeCommit() {
        File commitFile = Utils.join(COMMITS_DIR, this.getCommitID());
        Utils.writeObject(commitFile, this);
    }
}
