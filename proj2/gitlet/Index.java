package gitlet;

import java.io.File;

/** Index.java handles Gitlet's staging area, which tracks files that have
 * been added via Repository.addCommand().
 * */
public class Index {
    public static final File INDEX_DIR = Utils.join(Repository.GITLET_DIR, "/index");
}
