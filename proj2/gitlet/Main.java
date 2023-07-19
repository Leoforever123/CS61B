package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author LEO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static final int THREE = 3;
    public static void main(String[] args) {
        String firstArg = args[0];
        switch (firstArg) {
            case "init":
                if (Repository.GITLET_DIR.exists()) {
                    System.out.println("A Gitlet version-control system already exists in the current directory.");
                } else {
                    Repository repo = new Repository();
                    Utils.writeObject(Repository.EXCLUDE, repo);
                }
                break;
            case "add":
                Repository repo = Utils.readObject(Repository.EXCLUDE, Repository.class);
                repo.add(args[1]);
                Utils.writeObject(Repository.EXCLUDE, repo);
                break;
            case "commit":
                if (args[1].equals("")) {
                    System.out.println("Please enter a commit message.");
                } else {
                    Repository repo1 = Utils.readObject(Repository.EXCLUDE, Repository.class);
                    repo1.commit(args[1]);
                    Utils.writeObject(Repository.EXCLUDE, repo1);
                }
                break;
            case "log":
                Repository repo2 = Utils.readObject(Repository.EXCLUDE, Repository.class);
                repo2.log();
                Utils.writeObject(Repository.EXCLUDE, repo2);
                break;
            case "restore":
                Repository repo3 = Utils.readObject(Repository.EXCLUDE, Repository.class);
                if (args.length <= 2) {
                    System.out.println("Incorrect operands.");
                    break;
                }
                if (args.length == THREE) {
                    if (!args[1].equals("--")) {
                        System.out.println("Incorrect operands.");
                        break;
                    } else {
                        repo3.restore(args[2]);
                    }
                } else {
                    if (!args[2].equals("--")) {
                        System.out.println("Incorrect operands.");
                        break;
                    } else {
                        repo3.restore(args[1], args[THREE]);
                    }
                }
                Utils.writeObject(Repository.EXCLUDE, repo3);
                break;
            default:
        }

    }
}
