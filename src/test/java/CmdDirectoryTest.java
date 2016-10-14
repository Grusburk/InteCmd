import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CmdDirectoryTest {
    private LSCommand cmdLS;
    private String defaultPath;
    private File mockedFile;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUpTests() {
        defaultPath = "/Program Files/";

//        File mockedFile = Mockito.mock(File.class);
//        Mockito.when(mockedFile.exists()).thenReturn(true);
    }

    @Test (expected = NullPointerException.class)
    public void theFileShouldNotBeEmpty() {
        cmdLS = new LSCommand("");
        cmdLS.getFile().length();
    }

    @Test (expected = NullPointerException.class)
    public void theFileShouldNotBeNull() {
        cmdLS = new LSCommand("");
        cmdLS.getFile();
    }

    @Test(expected = NullPointerException.class)
    public void theDirectoryListShouldNotBeNull() {
        cmdLS = new LSCommand("");
        cmdLS.getDirectories();
    }

    @Test(expected = NullPointerException.class)
    public void theDirectoryListShouldNotBeEmpty() {
        cmdLS = new LSCommand("");
        cmdLS.getDirectories();
    }

    @Test(expected = NullPointerException.class)
    public void theFileListShouldNotBeNull() {
        cmdLS = new LSCommand("");
        cmdLS.getFiles();
    }

    @Test
    public void theFileListShouldFindAllFiles () throws IOException {
        final File file1 = temporaryFolder.newFile("file1.txt");
        final File file2 = temporaryFolder.newFile("file2.txt");
        final File file3 = temporaryFolder.newFile("file3.txt");
        cmdLS = new LSCommand(temporaryFolder.getRoot().getPath());
        assertEquals(cmdLS.getFiles().size(), 3);
    }

    @Test
    public void theDirectoryListShouldFindAllDirectories () throws IOException {
        final File dir1 = temporaryFolder.newFolder("directory1");
        final File dir2 = temporaryFolder.newFolder("directory2");
        final File dir3 = temporaryFolder.newFolder("directory3");
        cmdLS = new LSCommand(temporaryFolder.getRoot().getPath());
        assertEquals(cmdLS.getDirectories().size(), 3);
    }

    @Test
    public void theFileAndDirectoryListShouldFindAll () throws IOException {
        final File file1 = temporaryFolder.newFile("file1.txt");
        final File file2 = temporaryFolder.newFile("file2.txt");
        final File file3 = temporaryFolder.newFile("file3.txt");
        final File dir1 = temporaryFolder.newFolder("directory1");
        final File dir2 = temporaryFolder.newFolder("directory2");
        final File dir3 = temporaryFolder.newFolder("directory3");
        cmdLS = new LSCommand(temporaryFolder.getRoot().getPath());
        assertEquals(cmdLS.getDirectories().size() + cmdLS.getFiles().size(), 6);
    }
}
