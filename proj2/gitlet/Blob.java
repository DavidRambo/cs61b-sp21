package gitlet;

import java.io.File;
import java.io.Serializable;

/** Represents a gitlet blob.
 * When a file in the CWD is staged for addition, a copy of it is created
 * and named after the SHA-1 hash code of its contents. A Commit's
 * snapshot of the CWD associates filenames with these blobs by way of their
 * SHA-1 names. */
public class Blob implements Serializable {
    private String blobID;
    private String contents;

    public Blob(String contents) {
        this.contents = contents;
        this.blobID = Utils.sha1(contents);
    }

    public String getID() {
        return this.blobID;
    }
}