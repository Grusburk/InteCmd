package intecmd.commands;

import intecmd.CurrentDirectory;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public final class ChangeDirectoryCommand {

    private final static String CMD_PATTERN_PARENT_DIRECTORY = "..";
    private final static String PATTERN_ROOT_WINDOWS = "[a-zA-Z]{1}:\\\\{0,1}+";
    private final static String PATTERN_ROOT_UNIX = "/";
    private final static String PATTERN_WINDOWS_DIRECTORY = "^[^<>:\"/\\\\|?*]*$";
    private final static String PATTERN_UNIX_DIRECTORY = "^[^/]$";
    private final static String CMD_PATTERN_HOME_DIRECTORY = "~";

    private CurrentDirectory currentDirectory = new CurrentDirectory();

    public ChangeDirectoryCommand(String[] data) {
        try {
            switch (data.length) {
                case 1:
                    homeDirectory();
                    break;
                case 2:
                    readOptions(data);
                    break;
                default:
                    moveDown(data);
                    break;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public ChangeDirectoryCommand() {
        this(new String[]{""});
    }

    private void readOptions(String[] options) {
        if (options[1].equals(CMD_PATTERN_PARENT_DIRECTORY))
            moveUp();
        else if (options[1].equals(CMD_PATTERN_HOME_DIRECTORY))
            homeDirectory();
        else if (options[1].matches(PATTERN_WINDOWS_DIRECTORY) || options[1].matches(PATTERN_UNIX_DIRECTORY))
            moveDown(options[1]);
        else if (options[1].matches(PATTERN_ROOT_WINDOWS) || options[1].matches(PATTERN_ROOT_UNIX))
            changeRoot(options[1].toLowerCase(Locale.ROOT));
        else
            throw new IllegalArgumentException("No such file or directory.");
    }

    private void homeDirectory() {
        currentDirectory.setCurrentDirectory(System.getProperty("user.home"));
    }

    private void moveUp() {
        String[] pathParts = splitPath();
        if (systemIsWindows()) {
            moveUpWindowsPath(pathParts);
        } else {
            moveUpUnixPath(pathParts);
        }
    }

    private void moveUpWindowsPath(String[] pathParts) {
        StringBuilder stringBuilder = new StringBuilder(pathParts[0]);
        for (int i = 1; i < pathParts.length - 1; i++) {
            stringBuilder.append(CurrentDirectory.SEPARATOR);
            stringBuilder.append(pathParts[i]);
        }
        currentDirectory.setCurrentDirectory(stringBuilder.toString());
    }

    private void moveUpUnixPath(String[] pathParts) {
        StringBuilder stringBuilder = new StringBuilder(PATTERN_ROOT_UNIX);
        for (int i = 1; i < pathParts.length - 2; i++) {
            stringBuilder.append(pathParts[i]);
            stringBuilder.append(CurrentDirectory.SEPARATOR);
        }
        stringBuilder.append(pathParts[pathParts.length - 2]);
        currentDirectory.setCurrentDirectory(stringBuilder.toString());
    }

    private String[] splitPath() {
        return System.getProperty("os.name").startsWith("Windows") ? windowsSplitPath() : unixSplitPath();
    }

    private String[] windowsSplitPath() {
        return currentDirectory.toString().split(CurrentDirectory.SEPARATOR + CurrentDirectory.SEPARATOR);
    }

    private String[] unixSplitPath() {
        return currentDirectory.toString().split(CurrentDirectory.SEPARATOR);
    }

    private boolean systemIsWindows() {
        return System.getProperty("os.name").startsWith("Windows");
    }

    private void moveDown(String[] splitDirectory) {
        String directory = splitDirectory[1];
        StringBuilder stringBuilder = new StringBuilder(directory);
        for (int i = 2; i < splitDirectory.length; i++) {
            stringBuilder.append(" ");
            stringBuilder.append(splitDirectory[i]);
        }
        moveDown(stringBuilder.toString());
    }

    void moveDown(String directory) {
        if (!directoryExists(directory))
            throw new IllegalArgumentException("No such file or directory.");
        currentDirectory.setCurrentDirectory(currentDirectory.toString() + CurrentDirectory.SEPARATOR + directory);
    }

    private boolean directoryExists(String directory) {
        ArrayList<File> files = new LSCommand(currentDirectory.toString()).getDirectories();
        for (File file : files)
            if (file.getName().equals(directory))
                return true;
        return false;
    }

    void changeRoot(String root) {
        if (!rootExists(root))
            throw new IllegalArgumentException("No such file or directory.");
        else
            currentDirectory.setCurrentDirectory(root.toUpperCase(Locale.ROOT));

    }

    private boolean rootExists(String root) {
        for (File file : File.listRoots())
            if (file.toString().toLowerCase(Locale.ROOT).equals(root.toLowerCase(Locale.ROOT)) ||
                    file.toString().toLowerCase(Locale.ROOT).equals(root.toLowerCase(Locale.ROOT) + CurrentDirectory.SEPARATOR))
                return true;
        return false;
    }
}
