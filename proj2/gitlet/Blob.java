package gitlet;

import java.io.Serializable;
import java.io.File;

public class Blob implements Serializable {
    /* Directory in which to store blobs. */
    public static final File BLOBS_DIR = Utils.join(Repository.GITLET_DIR, "/blobs");
    // file contents in byte[] array
    private final byte[] contents;
    // name of the blob as SHA-1 hash of the file
    private final String blobName;

    /** Creates a new Blob out of a file's byte array. */
    public Blob(File file) {
        // Store file as serialized byte array
        this.contents = Utils.readContents(file);
        // Calculate hash for ID.
        this.blobName = Utils.sha1((Object) contents);
    }

    /** Returns the name of the blob object. */
    public String getName() {
        return this.blobName;
    }

    /** Writes the byte[] array contents of the blob to the file system. */
    public void save() {
        // Write blob object to blobs subdirectory.
        File blobFile = Utils.join(BLOBS_DIR, this.blobName);
        Utils.writeContents(blobFile, (Object) this.contents);
    }

    /** Reads the contents of a blob in its serialized byte array format.
     * @param blobName name of the blob file to load. */
    public static byte[] readBlob(String blobName) {
        File blobFile = Utils.join(BLOBS_DIR, blobName);
        return Utils.readContents(blobFile);
    }

    /** Returns the contents of the blob in String format. */
    public static String readBlobAsString(String blobName) {
        File blobFile = Utils.join(BLOBS_DIR, blobName);
        return Utils.readContentsAsString(blobFile);
    }
}
