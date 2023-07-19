package gitlet;


import java.text.SimpleDateFormat;
import java.util.*;

/** Represents a gitlet commit object.
 *  does at a high level.
 *
 *  @author LEO
 */
public class Commit extends LinkedList<String> {
    /**
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;

    private String parent;

    HashMap<String, String> blobs;

    private String timeStamp;


    public Commit(String message, String parent) {
        timeStamp = initTimeStamp();
        blobs = new HashMap<>();
        this.message = message;
        this.parent = parent;
    }

    public String initTimeStamp() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        return dateFormat.format(date);
    }

    public String toHash() {
        List<Object> hash = new LinkedList<>();
        hash.add(timeStamp);
        hash.add(message);
        for (String val : blobs.values()) {
            hash.add(val);
        }
        return Utils.sha1(hash);
    }

    public String getMessage() {
        return message;
    }

    public String getParent() {
        return parent;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

}
