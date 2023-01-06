package gitlet;

import java.io.File;

import static gitlet.Utils.join;

/** The HEAD class is responsible for interacting with the HEAD, which is a file
 * in the Gitlet repository's .gitlet directory. It contains the name of the
 * branch currently checked out.
 * */
public class HEAD {

    public static final File HEAD_FILE = join(Repository.GITLET_DIR, "HEAD");

    /** Sets the name of the currently checked out branch by updating the HEAD_FILE
     * with the path to that branch's file, which contains a reference to its most
     * recent commit. */
    public static void pointToBranch(String branch) {
        // TODO: Check for untracked file

        // Check whether that branch is already checked out.
        if (branch.equals(getCurrentHead())) {
            System.out.println("No need to check out the current branch.");
            System.exit(0);
        }
        // Ensure branch exists.
        File branch_file = Utils.join(Branch.BRANCHES_DIR, "branch");
        if (!branch_file.exists()) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        // Set HEAD to branch by writing its name in the HEAD_FILE.
        Utils.writeContents(HEAD_FILE, branch);
    }

    /** Reads the HEAD_FILE for the name of the currently checked out branch.
     * In Git, this would be the name of a commit. */
    public static String getCurrentHead() {
        return Utils.readContentsAsString(HEAD_FILE);
    }

}
