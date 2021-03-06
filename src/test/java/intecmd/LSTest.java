package intecmd;

import intecmd.commands.LSCommand;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class LSTest {
    private LSCommand cmdLS;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    String osName = System.getProperty ("os.name");

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUpTests() {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void theFileorDirectoryShouldNotBeEmpty() {
        String command = "ls";
        String expectedOutput = "Directories: No directories in this directory\r" +
                "\nFiles: No files in this directory";
        cmdLS = new LSCommand(command.split(" ") , temporaryFolder.getRoot().getPath());
        osDependent(expectedOutput);
    }

    @Test(expected = NullPointerException.class)
    public void theDirectoryListShouldNotBeNull() {
        cmdLS = new LSCommand(null);
        cmdLS.getDirectories();
    }

    @Test(expected = NullPointerException.class)
    public void theFileListShouldBeNull() {
        cmdLS = new LSCommand(null);
        cmdLS.getFiles();
    }

    @Test
    public void theSetFileMethodShouldFindAllFiles () throws IOException {
        setUpTempFiles();
        cmdLS = new LSCommand(temporaryFolder.getRoot().getPath());
        assertEquals(cmdLS.getFiles().size(), 3);
    }

    @Test
    public void theFileListShouldFindAllFiles () throws IOException {
        setUpTempFiles();
        cmdLS = new LSCommand(temporaryFolder.getRoot().getPath());
        assertEquals(3,cmdLS.getFiles().size());
    }

    @Test
    public void theDirectoryListShouldFindAllDirectories () throws IOException {
        setUpTempDirectories();
        cmdLS = new LSCommand(temporaryFolder.getRoot().getPath());
        assertEquals(3,cmdLS.getDirectories().size());
    }

    @Test
    public void theFileAndDirectoryListShouldFindAll () throws IOException {
        setUpTempFiles();
        setUpTempDirectories();
        cmdLS = new LSCommand(temporaryFolder.getRoot().getPath());
        assertEquals(6,cmdLS.getDirectories().size() + cmdLS.getFiles().size());
    }

    @Test
    public void theLFlagShouldListAllContent () throws IOException {
        String command = "ls -l";
        final File file1 = temporaryFolder.newFile("file1.txt");
        final File file2 = temporaryFolder.newFile("file2.txt");
        final File file3 = temporaryFolder.newFile("file3.txt");
        final File dir1 = temporaryFolder.newFolder("directory1");
        final File dir2 = temporaryFolder.newFolder("directory2");
        final File dir3 = temporaryFolder.newFolder("directory3");
        cmdLS = new LSCommand(command.split(" ") , temporaryFolder.getRoot().getPath());
        String expectedOutput = "Directories:\r\ndirectory1\r\ndirectory2\r\ndirectory3" +
                "\r\nFiles:\r\nfile1.txt\r\nfile2.txt\r\nfile3.txt";
        osDependent(expectedOutput);
    }

    @Test
    public void theFFlagShouldListAllContent () throws IOException {
        String command = "ls -f";
        setUpTempFiles();
        cmdLS = new LSCommand(command.split(" ") , temporaryFolder.getRoot().getPath());
        String expectedOutput = "Files: \r\nfile1.txt file2.txt file3.txt";
        osDependent(expectedOutput);
    }
    @Test
    public void theLFFlagShouldListAllContent () throws IOException {
        String command = "ls -lf";
        setUpTempFiles();
        cmdLS = new LSCommand(command.split(" ") , temporaryFolder.getRoot().getPath());
        String expectedOutput = "Files:\r\nfile1.txt\r\nfile2.txt\r\nfile3.txt";
        osDependent(expectedOutput);
    }
    @Test
    public void theDFlagShouldListAllContent () throws IOException {
        String command = "ls -d";
        setUpTempDirectories();
        cmdLS = new LSCommand(command.split(" ") , temporaryFolder.getRoot().getPath());
        String expectedOutput = "Directories: directory1 directory2 directory3";
        osDependent(expectedOutput);
    }

    @Test
    public void theLdFlagShouldListAllContent () throws IOException {
        String command = "ls -ld";
        setUpTempFiles();
        setUpTempDirectories();
        cmdLS = new LSCommand(command.split(" ") , temporaryFolder.getRoot().getPath());
        String expectedOutput = "Directories:\r\ndirectory1\r\ndirectory2\r\ndirectory3";
        osDependent(expectedOutput);
    }

    @Test
    public void theLdCommandShouldOnlyAllowOneFlag () throws IOException {
        String command = "ls -ld -lf";
        cmdLS = new LSCommand(command.split(" ") , temporaryFolder.getRoot().getPath());
        assertEquals("Too many flags. Try -help", outContent.toString().trim());
    }

    @Test
    public void theResultForWhenFlagNotRecognized () throws IOException {
        String command = "ls -";
        setUpTempFiles();
        setUpTempDirectories();
        cmdLS = new LSCommand(command.split(" ") , temporaryFolder.getRoot().getPath());
        String expectedOutput = "Flag not recognized. Try -help";
        osDependent(expectedOutput);
    }

    @Test
    public void theDefaultOutputIfNoFlagShouldBe () throws IOException {
        String command = "ls";
        setUpTempFiles();
        setUpTempDirectories();
        cmdLS = new LSCommand(command.split(" ") , temporaryFolder.getRoot().getPath());
        String expectedOutput = "Directories: directory1 directory2 directory3 Files: file1.txt file2.txt file3.txt";
        osDependent(expectedOutput);
    }

    @Test
    public void theHelpFlagShouldHaveDefaultOutput() {
        String command = "ls -help";
        cmdLS = new LSCommand(command.split(" "), temporaryFolder.getRoot().getPath());
        assertEquals(cmdLS.help(), outContent.toString().trim());
    }

    private void osDependent(String expectedOutput) {
        if (osName.startsWith("Windows")){
            assertEquals(expectedOutput, outContent.toString().trim());
        }else {
            assertEquals(expectedOutput.toString().replaceAll("\r\n", "\n"), outContent.toString().trim());
        }
    }

    private void setUpTempFiles() throws IOException {
        final File file1 = temporaryFolder.newFile("file1.txt");
        final File file2 = temporaryFolder.newFile("file2.txt");
        final File file3 = temporaryFolder.newFile("file3.txt");
    }

    private void setUpTempDirectories() throws IOException {
        final File dir1 = temporaryFolder.newFolder("directory1");
        final File dir2 = temporaryFolder.newFolder("directory2");
        final File dir3 = temporaryFolder.newFolder("directory3");
    }

}