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

    /** Stages file for addition.
     * @param file the file to be added
     * */
    public void stage(String filename, String blobID) {
        additions.put(filename, blobID);
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
}
