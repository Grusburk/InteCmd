import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;

public class LSCommand{
    private final String DEFAULTPATH = ".";
    private File file = new File(DEFAULTPATH);
    private ArrayList<File> directories = new ArrayList<>();
    private ArrayList<File> files = new ArrayList<>();

    public LSCommand (String pathToFile){
        this.file = new File(pathToFile);
        setDirectories();
        setFiles();
    }

    public LSCommand(String[] commands, String pathToFile) {
        this.file = new File(pathToFile);
        setDirectories();
        setFiles();
        if (commands.length > 1){
            switch (commands[1]){
                case "-l":
                    System.out.println("Directories");
                    listDirectoriesTopDown();
                    System.out.println("Files");
                    listFilesTopDown();
                    break;
                case "-f":
                    System.out.println("Files");
                    System.out.println(getFiles());
                    break;
                case "-lf":
                    System.out.println("Files:");
                    listFilesTopDown();
                    break;
                case "-d":
                    System.out.print("Directories: ");
                    System.out.println(getDirectories());
                    break;
                case "-ld":
                    System.out.println("Directories:");
                    listDirectoriesTopDown();
                    break;
                default:
                    System.out.println("are u retarded?");
            }
        }else {
            System.out.print("Directories: ");
            System.out.println(getDirectories().size() > 0 ? getDirectories() :
                    "No directories in this directory");
            System.out.print("Files: ");
            System.out.println(getFiles().size() > 0 ? getFiles(): "No files in this directory");
        }

    }

    public void setDirectories (){
        directories.addAll(Arrays.asList(file.listFiles((dir, name) -> new File(dir, name).isDirectory())));
    }

    public void setFiles () {
        files.addAll(Arrays.asList(file.listFiles((current, name) -> new File(current, name).isFile())));
    }

    public ArrayList<File> getFiles() {
        if (files == null){
            throw new NullPointerException();
        }else {
        return files;
        }
    }

    public ArrayList<File> getDirectories() {
        if (directories == null){
            throw new NullPointerException();
        }else{
            return directories;
        }
    }

    private void listFilesTopDown() {
        files.forEach(file1 -> System.out.println(file1.getName()));
    }

    private void listDirectoriesTopDown() {
        directories.forEach(directories1 -> System.out.println(directories1.getName()));
    }
}
