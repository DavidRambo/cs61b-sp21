package gitlet;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.io.Serializable;

/** Handles Gitlet's staging area with a HashMap of filenames and their blob
 * objects (staged for addition) and a HashSet of filenames (staged for removal).
 */
public class Index implements Serializable {
    // <filename, blobID> files staged for addition
    HashMap<String, String> additions;
    HashSet<String> removals;

    /** Constructor method for the Index class. */
    public Index() {
        additions = new HashMap<String, String>();
        removals = new HashSet<String>();
    }

    public HashMap<String, String> getAdditions() {
        return additions;
    }

    public HashSet<String> getRemovals() {
        return removals;
    }

    /** Stages file for addition. If it was previously staged for removal, then remove from
     * removals and only stage if modified.
     * @param filename the name of the file to be added
     * */
    public void stage(String filename, String blobID) {
        additions.put(filename, blobID);
    }

    /** If file is currently staged, then it unstages file for addition.
     * Otherwise, it stages for removal.
      */
    public void remove(String filename) {
        if (isStaged(filename)) {
            /* Remove from staging area. */
            getAdditions().remove(filename);
        } else {
            /* Add to removals */
            getRemovals().add(filename);
            /* Remove from working directory. */
            File file = Utils.join(Repository.CWD, filename);
            file.delete();
        }
        save();
    }

    /** Writes the Index object to the file system. */
    public void save() {
        Utils.writeObject(Repository.INDEX, this);
    }

    /** Loads the staging area. */
    public static Index load() {
        return Utils.readObject(Repository.INDEX, Index.class);
    }

    /** Clears the staging area. */
    public void clear() {
        additions.clear();
        removals.clear();
    }

    /** Checks whether the file is staged for addition. */
    public boolean isStaged(String filename) {
        return getAdditions().containsKey(filename);
    }

    /** Returns the contents of a file staged for addition at the time that it was staged. */
    public String getContent(String filename) {
        assert getAdditions().containsKey(filename);
        String blobID = getAdditions().get(filename);
        File stagedBlobFile = Utils.join(Repository.BLOBS_DIR, blobID);
        assert stagedBlobFile.exists();
        Blob stagedBlob = Utils.readObject(stagedBlobFile, Blob.class);
        return stagedBlob.getContents();
    }
}
