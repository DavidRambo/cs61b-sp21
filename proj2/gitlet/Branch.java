package gitlet;

import java.io.File;

/** The Branch class handles plain files in the /refs subdirectory. These files contain
 * the names of branches in the Gitlet repository. */
public class Branch {
    public static final File BRANCHES_DIR = Utils.join(Repository.GITLET_DIR, "/refs");

    /** Overwrite a branch's most recent commit.
     * Parameters:
     * @branchName : String that names the branch to be updated.
     * @commitID : hash code of the commit to be written into the branch file.*/
    public static void updateCommit(String branchName, String commitID) {
        File branch = Utils.join(BRANCHES_DIR, branchName);
        Utils.writeContents(branch, commitID);
    }

    /** Returns the commitID of the branch. */
    public static String getBranchHead(String branchName) {
        File branch = Utils.join(BRANCHES_DIR, branchName);
        return Utils.readContentsAsString(branch);
    }
}
