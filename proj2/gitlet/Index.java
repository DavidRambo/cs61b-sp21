package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/** Index.java handles Gitlet's staging area, which tracks files that have
 * been added via Repository.addCommand().
 * */
public class Index implements Serializable {
    // Staging Area file
    public static final File INDEX = Utils.join(Repository.GITLET_DIR, "index");

    // Mapping of <file name, blob name> for files staged for addition
    private HashMap<String, String> stagedAdditions;
    // Map of names of files staged for removal
    private HashSet<String> stagedRemovals;

    /** Constructor method for the staging area.
     * This method is called by the Gitlet Repository.init method
     * to create an empty index file. */
    public Index() {
        stagedAdditions = new HashMap<>();
        stagedRemovals = new HashSet<>();
    }

    /** Save the staging area object. */
    public void save() {
        Utils.writeObject(INDEX, this);
    }

    /** Stages a file for addition to the gitlet repository. */
    public void stageFile(String fileName) {
        File file = Utils.join(Repository.CWD, fileName);

        /* 1. Create copy of file. */
        Blob newBlob = new Blob(file);
        String newBlobName = newBlob.getName();

        // 2. Read the staging area into runtime memory.
        HashMap<String, String> stagedAdditions = getAdditions();
        // Read current commit into runtime memory.
        String headID = Branch.getBranchHead(HEAD.getCurrentHead());
        Commit headCommit = Commit.loadCommit(headID);

        /* 3. Check the file's current status. If unchanged from current commit,
         * do not add to the staging area.
         * If already staged and unmodified, remove from the staging area. */
        if (headCommit.getBlobName(fileName).equals(newBlobName)) {
            return;
        }

        if (stagedAdditions.get(fileName).equals(newBlobName)) {
            stagedAdditions.remove(fileName);
            // Do I need to remove the corresponding blob from the .gitlet directory?
            return;
        }
        
        // Staging an already-staged file overwrites the previous entry in the staging 
        // area with the new contents.
        stagedAdditions.put(fileName, newBlobName);
        
        /* Write the new blob to the file system. */
        Utils.writeObject(Blob.BLOBS_DIR, newBlob);

        /* Save staging area. */
        save();
    }

    /** Returns the HashMap of staged files. */
    public HashMap<String, String> getAdditions() {
        return stagedAdditions;
    }

    /** Returns the HashMap of staged files' names. */
    public HashSet<String> getRemovals() {
        return stagedRemovals;
    }

    /** Loads the staging area from the gitlet directory. */
    public static Index load() {
        return Utils.readObject(INDEX, Index.class);
    }

    /** Clears the staging area. */
    public void clear() {
        stagedAdditions.clear();
        stagedRemovals.clear();
    }
}
