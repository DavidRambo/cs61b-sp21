package gitlet;

import java.io.File;

import static gitlet.Utils.join;

public class HEAD {

    public static final File HEAD_FILE = join(Repository.GITLET_DIR, "HEAD");

    /** Sets the name of the currently checked out branch by updating the HEAD_FILE
     * with the path to that branch's file, which contains a reference to its most
     * recent commit. */
    public static void pointToBranch(String branch) {
        Utils.writeContents(HEAD_FILE, branch);
    }

    /** Reads the HEAD_FILE for the name of the currently checked out branch.
     * In Git, this would be the name of a commit. */
    public static String getCurrentHead() {
        return Utils.readContentsAsString(HEAD_FILE);
    }

}
