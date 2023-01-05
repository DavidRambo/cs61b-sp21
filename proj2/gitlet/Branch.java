package gitlet;

import java.io.File;

public class Branch {
    public static final File BRANCHES_DIR = Utils.join(Repository.GITLET_DIR, "/refs");

    /** Overwrite a branch's most recent commit.
     * Parameters:
     * @master : String that names the branch to be updated.
     * @commitID : hash code of the commit to be written into the branch file.*/
    public static void updateCommit(String master, String commitID) {
        File branch = Utils.join(BRANCHES_DIR, master);
        Utils.writeContents(branch, commitID);
    }
}
