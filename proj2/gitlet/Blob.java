package gitlet;

import java.io.Serializable;
import java.io.File;

public class Blob implements Serializable {
    /* Directory in which to store blobs. */
    public static final File BLOBS_DIR = Utils.join(Repository.GITLET_DIR, "/blobs");
    // File as serialized byte array
    private final byte[] file;
    // name of the blob as SHA-1 hash of the file
    public final String blobName;

    /** Creates a new Blob out of a file's byte array. */
    public Blob(File file) {
        // Serialize file into SHA-1 hash.
        this.file = Utils.serialize(file);
        // Calculate hash for ID.
        blobName = Utils.sha1(this.file);
        // Write blob object to blobs subdirectory.
        File blobFile = Utils.join(BLOBS_DIR, blobName);
        Utils.writeObject(blobFile, this);
    }

    public String getName() {
        return this.blobName;
    }
}
