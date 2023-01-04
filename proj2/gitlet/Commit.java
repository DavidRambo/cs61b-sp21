package gitlet;

// TODO: any imports you need here

import java.util.Date;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author David Rambo
 */
public class Commit {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private Date timestamp;
    private Commit parent;

    /* TODO: fill in the rest of this class. */

    // TODO: Generate log message.

    /* Constructor method. */
    public Commit(String message, Commit parent) {
        this.message = message;
        this.parent = parent;
        // Check whether it is initial commit, and if so, then set timestamp to 0.
        if (this.parent == null) {
            this.timestamp = new Date(0);
        } else {
            // Get current time and store it as timestamp String.
            this.timestamp = new Date();
        }
    }

    public String getMessage() {
        return this.message;
    }

    public String getTimestamp() {
        return this.timestamp.toString();
    }

    public Commit getParent() {
        return this.parent;
    }
}
