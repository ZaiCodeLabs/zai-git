package za.co.zaicodelabs.zaigit.service;

import org.eclipse.jgit.api.Git;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class GitServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void reportsAddedModifiedAndRemovedFiles() throws Exception {
        try (Git ignored = initializeRepository();
             GitService service = serviceForRepository()) {
            Path file = tempDir.resolve("example.txt");
            Files.writeString(file, "first\n");
            assertTrue(service.getDetailedStatus().untracked.contains("example.txt"));

            service.commit("feat: add example");
            Files.writeString(file, "second\n");
            assertTrue(service.getDetailedStatus().modified.contains("example.txt"));

            Files.delete(file);
            assertTrue(service.getDetailedStatus().missing.contains("example.txt"));
        }
    }

    @Test
    void diffIncludesContentButExcludesSensitiveFiles() throws Exception {
        try (Git ignored = initializeRepository();
             GitService service = serviceForRepository()) {
            Files.writeString(tempDir.resolve("feature.txt"), "useful change\n");
            service.commit("feat: add feature");
            Files.writeString(tempDir.resolve("feature.txt"), "meaningful implementation\n");
            Files.writeString(tempDir.resolve(".env"), "API_SECRET=do-not-send\n");

            String diff = service.getDiff();

            assertTrue(diff.contains("meaningful implementation"));
            assertFalse(diff.contains("do-not-send"));
        }
    }

    @Test
    void stashPopAppliesAndDropsTheStash() throws Exception {
        try (Git ignored = initializeRepository();
             GitService service = serviceForRepository()) {
            Path file = tempDir.resolve("example.txt");
            Files.writeString(file, "original\n");
            service.commit("feat: add example");
            Files.writeString(file, "stashed\n");

            service.stashChanges();
            assertFalse(service.listStashes().contains("No stashes"));

            service.stashPop();
            assertEquals("stashed\n", Files.readString(file));
            assertTrue(service.listStashes().contains("No stashes"));
        }
    }

    @Test
    void rejectsDirectoriesOutsideARepository() {
        assertThrows(IllegalStateException.class, () -> new GitService(tempDir));
    }

    private GitService serviceForRepository() {
        return new GitService(tempDir);
    }

    private Git initializeRepository() throws Exception {
        Git git = Git.init().setDirectory(tempDir.toFile()).call();
        git.getRepository().getConfig().setString("user", null, "name", "ZaiGit Test");
        git.getRepository().getConfig().setString("user", null, "email", "test@example.com");
        git.getRepository().getConfig().save();
        return git;
    }
}
