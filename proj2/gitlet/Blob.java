package gitlet;

import java.io.Serializable;
import java.io.File;

public class Blob implements Serializable {

    /* Directory in which to store blobs. */
    public static final File BLOBS_DIR = Utils.join(Repository.GITLET_DIR, "/blobs");
}
