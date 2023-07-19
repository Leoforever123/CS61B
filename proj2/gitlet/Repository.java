package gitlet;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static gitlet.Utils.*;


/** Represents a gitlet repository.
 *  does at a high level.
 *
 *  @author LEO
 */
public class Repository implements Serializable {
    /**
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    public static final File STAGED = join(GITLET_DIR, "staged");

    public static final File COMMITTED = join(GITLET_DIR, "committed");

    public static final File BLOBS = join(GITLET_DIR, "blobs");

    public static final File EXCLUDE = join(GITLET_DIR, "exclude.txt");


    String rootCommit;

    String currBranch;

    ArrayList<String> currCommit;

    HashSet<String> addList;

    HashSet<String> removeList;

    HashMap<String, Commit> commitList;

    HashMap<String, Integer> branchList;


    public Repository() {

        GITLET_DIR.mkdir();
        STAGED.mkdir();
        BLOBS.mkdir();
        try {
            EXCLUDE.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        commitList = new HashMap<>();
        branchList = new HashMap<>();
        addList = new HashSet<>();
        currCommit = new ArrayList<>();

        branchList.put("main", 0);
        currBranch = "main";

        Commit temp = new Commit("initial commit", null);
        addCommit(temp);
        rootCommit = temp.toHash();
    }

    public void addCommit(Commit obj) {
        commitList.put(obj.toHash(), obj);
        int n = branchList.get(currBranch);
        if (currCommit.size() <= n) {
            currCommit.add(obj.toHash());
        } else {
            currCommit.set(n, obj.toHash());
        }
    }

    public void add(String fileName) {

        if (addList.contains(fileName)) {
            File past = Utils.join(STAGED, fileName);
            past.delete();
        } else {
            addList.add(fileName);
        }
        File file = Utils.join(CWD, fileName);
        File blob = Utils.join(STAGED, fileName);
        Utils.writeObject(blob, Utils.readContentsAsString(file));
        try {
            blob.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void commit(String message) {
        if (addList.size() == 0) {
            System.out.println("No changes added to the commit.");
            return;
        } else {
            int n = branchList.get(currBranch);
            Commit newcommit = new Commit(message, currCommit.get(n));
            initByAdd(newcommit);
            addCommit(newcommit);
        }
    }

    public void initByAdd(Commit obj) {
        for (String val : addList) {
            File temp = join(STAGED, val);
            String fileHash = Utils.sha1(Utils.readContents(temp));
            fileHash += ".txt";
            File des = join(BLOBS, fileHash);
            temp.renameTo(des);
            obj.blobs.put(val, fileHash);
        }
        addList.clear();
    }

    public void log() {
        String res = "";
        Commit temp = commitList.get(currCommit.get(branchList.get(currBranch)));
        res += """
                ===
                ${HEADER}
                ${DATE}
                ${MESSAGE}
                
                """
                .replace("${MESSAGE}", temp.getMessage())
                .replace("${HEADER}", "commit " + temp.toHash())
                .replace("${DATE}", "Date: " + temp.getTimeStamp());
        while(temp.getParent() != null) {
            temp = commitList.get(temp.getParent());
            res += """
                ===
                ${HEADER}
                ${DATE}
                ${MESSAGE}
                
                """
                    .replace("${MESSAGE}",temp.getMessage())
                    .replace("${HEADER}", "commit " + temp.toHash())
                    .replace("${DATE}", "Date: " + temp.getTimeStamp());
        }
        System.out.println(res);
    }

    public void restore(String fileName) {
        Commit temp = commitList.get(currCommit.get(branchList.get(currBranch)));
        if (!temp.blobs.containsKey(fileName)) {
            System.out.println("File does not exist in that commit.");
        } else {
            String blobName = temp.blobs.get(fileName);
            String content = Utils.readObject(Utils.join(BLOBS, blobName), String.class);
            File des = Utils.join(CWD, fileName);
            if (des.exists()) {
                des.delete();
            }
            Utils.writeContents(des, content);
            try {
                des.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void restore(String id, String fileName) {
        Commit temp = commitList.get(id);
        if (!temp.blobs.containsKey(fileName)) {
            System.out.println("File does not exist in that commit.");
        } else {
            String blobName = temp.blobs.get(fileName);
            String content = Utils.readObject(Utils.join(BLOBS, blobName), String.class);
            File des = Utils.join(CWD, fileName);
            if (des.exists()) {
                des.delete();
            }
            Utils.writeContents(des, content);
            try {
                des.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void remove(String filename) {
        if (addList.contains(filename)) {
            addList.remove(filename);
            return;
        }
        Commit temp = commitList.get(currCommit.get(branchList.get(currBranch)));
        if(temp.blobs.containsKey(filename)) {
            File waste = Utils.join(CWD, filename);
            if (waste.exists()) {
                waste.delete();
            }
            removeList.add(filename);
            return;
        }
        System.out.println("No reason to remove the file.");
    }
}
