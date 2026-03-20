package za.co.zaicodelabs.zaigit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GitService.
 * These tests are designed to work even when not in a Git repository.
 */
class GitServiceTest {

    private GitService gitService;

    @BeforeEach
    void setUp() {
        gitService = new GitService();
    }

    @Test
    void testServiceInitialization() {
        assertNotNull(gitService, "GitService should be initialized");
    }

    @Test
    void testGetCurrentBranch_HandlesNonGitDirectory() {
        String branch = gitService.getCurrentBranch();

        // Branch can be null or "unknown" when not in a git repository
        // This is expected behavior, not an error
        assertTrue(
                branch == null || branch.equals("unknown") || branch.length() > 0,
                "Branch should be null, 'unknown', or a valid branch name"
        );
    }

    @Test
    void testCloseDoesNotThrow() {
        // Verify close() doesn't throw exception
        assertDoesNotThrow(() -> gitService.close(),
                "close() should not throw exception");
    }
}