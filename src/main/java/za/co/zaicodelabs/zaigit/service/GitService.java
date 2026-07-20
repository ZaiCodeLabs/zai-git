package za.co.zaicodelabs.zaigit.service;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class GitService implements AutoCloseable {

    private static final int MAX_AI_DIFF_LENGTH = 12_000;

    private Git git;
    private Repository repository;

    public GitService() {
        try {
            initializeRepository();
        } catch (Exception e) {
            git = null;
            repository = null;
        }
    }

    GitService(Path workTree) {
        try {
            repository = new FileRepositoryBuilder()
                    .findGitDir(workTree.toFile())
                    .build();
            if (repository.getDirectory() == null) {
                throw new IOException("No .git directory found from " + workTree);
            }
            git = new Git(repository);
        } catch (Exception e) {
            throw new IllegalStateException("Not inside a Git repository: " + workTree, e);
        }
    }

    private void initializeRepository() throws IOException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        builder.readEnvironment().findGitDir();
        if (builder.getGitDir() == null) {
            throw new IOException("No .git directory found");
        }
        repository = builder.build();
        git = new Git(repository);
    }

    private Git requireGit() {
        if (git == null) {
            throw new IllegalStateException("Not inside a Git repository");
        }
        return git;
    }

    private Repository requireRepository() {
        if (repository == null) {
            throw new IllegalStateException("Not inside a Git repository");
        }
        return repository;
    }

    public void pull() throws GitAPIException {
        requireGit().pull().call();
    }

    public void push() throws GitAPIException {
        requireGit().push().call();
    }

    public void commitAndPush(String message) throws GitAPIException {
        commit(message);
        push();
    }

    public void commit(String message) throws GitAPIException {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Commit message cannot be empty");
        }
        // Add all changes
        requireGit().add().addFilepattern(".").call();

        // Also add removed files
        requireGit().add().addFilepattern(".").setUpdate(true).call();

        // Commit
        requireGit().commit().setMessage(message.trim()).call();
    }

    public boolean hasChanges() throws GitAPIException {
        Status status = requireGit().status().call();
        return status.hasUncommittedChanges() || !status.getUntracked().isEmpty();
    }

    public String getStatus() throws GitAPIException {
        Status status = requireGit().status().call();
        StringBuilder sb = new StringBuilder();

        sb.append("Branch: ").append(getCurrentBranch()).append("\n\n");

        if (status.getAdded().isEmpty() && status.getChanged().isEmpty() &&
                status.getRemoved().isEmpty() && status.getUntracked().isEmpty() &&
                status.getModified().isEmpty() && status.getMissing().isEmpty()) {
            sb.append("Working tree clean\n");
        } else {
            if (!status.getAdded().isEmpty()) {
                sb.append("Added:\n");
                status.getAdded().forEach(f -> sb.append("  + ").append(f).append("\n"));
            }
            if (!status.getModified().isEmpty()) {
                sb.append("Modified:\n");
                status.getModified().forEach(f -> sb.append("  M ").append(f).append("\n"));
            }
            if (!status.getChanged().isEmpty()) {
                sb.append("Changed:\n");
                status.getChanged().forEach(f -> sb.append("  M ").append(f).append("\n"));
            }
            if (!status.getRemoved().isEmpty()) {
                sb.append("Removed:\n");
                status.getRemoved().forEach(f -> sb.append("  - ").append(f).append("\n"));
            }
            if (!status.getMissing().isEmpty()) {
                sb.append("Deleted (unstaged):\n");
                status.getMissing().forEach(f -> sb.append("  - ").append(f).append("\n"));
            }
            if (!status.getUntracked().isEmpty()) {
                sb.append("Untracked:\n");
                status.getUntracked().forEach(f -> sb.append("  ? ").append(f).append("\n"));
            }
        }

        return sb.toString();
    }

    public String getCurrentBranch() {
        try {
            return requireRepository().getBranch();
        } catch (IOException e) {
            return "unknown";
        }
    }

    public String getDiff() throws GitAPIException, IOException {
        StringBuilder diff = new StringBuilder();
        Status status = requireGit().status().call();

        // Get all changed files
        List<String> allChanges = new ArrayList<>();
        allChanges.addAll(status.getAdded());
        allChanges.addAll(status.getChanged());
        allChanges.addAll(status.getModified());
        allChanges.addAll(status.getRemoved());
        allChanges.addAll(status.getMissing());
        allChanges.addAll(status.getUntracked());

        if (allChanges.isEmpty()) {
            return "No changes";
        }

        // Build a summary and include a bounded patch so the model can infer intent.
        diff.append("Repository changes summary:\n\n");

        if (!status.getAdded().isEmpty() || !status.getUntracked().isEmpty()) {
            diff.append("New files added:\n");
            status.getAdded().forEach(f -> diff.append("  + ").append(f).append("\n"));
            status.getUntracked().forEach(f -> diff.append("  + ").append(f).append("\n"));
        }

        if (!status.getModified().isEmpty() || !status.getChanged().isEmpty()) {
            diff.append("\nFiles modified:\n");
            status.getModified().forEach(f -> diff.append("  M ").append(f).append("\n"));
            status.getChanged().forEach(f -> diff.append("  M ").append(f).append("\n"));
        }

        if (!status.getRemoved().isEmpty() || !status.getMissing().isEmpty()) {
            diff.append("\nFiles removed:\n");
            status.getRemoved().forEach(f -> diff.append("  - ").append(f).append("\n"));
            status.getMissing().forEach(f -> diff.append("  - ").append(f).append("\n"));
        }

        diff.append("\nTotal changes: ").append(allChanges.size()).append(" file(s)\n");

        appendDiff(diff, false, "Unstaged patch");
        appendDiff(diff, true, "Staged patch");

        if (diff.length() > MAX_AI_DIFF_LENGTH) {
            return diff.substring(0, MAX_AI_DIFF_LENGTH) + "\n[diff truncated]";
        }

        return diff.toString();
    }

    private void appendDiff(StringBuilder target, boolean cached, String heading)
            throws GitAPIException {
        List<DiffEntry> entries = requireGit().diff().setCached(cached).call();
        if (entries.isEmpty()) {
            return;
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        for (DiffEntry entry : entries) {
            if (!isSensitivePath(entry.getOldPath()) && !isSensitivePath(entry.getNewPath())) {
                String path = DiffEntry.DEV_NULL.equals(entry.getNewPath())
                        ? entry.getOldPath() : entry.getNewPath();
                requireGit().diff()
                        .setCached(cached)
                        .setPathFilter(PathFilter.create(path))
                        .setOutputStream(output)
                        .call();
                if (output.size() >= MAX_AI_DIFF_LENGTH) {
                    break;
                }
            }
        }

        if (output.size() > 0) {
            target.append("\n").append(heading).append(":\n")
                    .append(output.toString(StandardCharsets.UTF_8));
        }
    }

    private boolean isSensitivePath(String path) {
        String lower = path.toLowerCase();
        return lower.contains(".env") || lower.endsWith(".pem") || lower.endsWith(".key")
                || lower.contains("credentials") || lower.contains("secret");
    }

    public List<String> getUnpushedCommits() throws GitAPIException, IOException {
        List<String> commits = new ArrayList<>();

        try {
            String currentBranch = getCurrentBranch();
            String remoteBranch = "origin/" + currentBranch;

            // Try to get remote ref
            Ref remoteRef = requireRepository().findRef(remoteBranch);

            if (remoteRef == null) {
                // No remote tracking branch, return recent commits
                Iterable<RevCommit> logs = requireGit().log().setMaxCount(5).call();
                for (RevCommit commit : logs) {
                    commits.add(commit.getShortMessage());
                }
                return commits;
            }

            // Get commits that are in local but not in remote
            ObjectId localId = requireRepository().resolve(currentBranch);
            ObjectId remoteId = remoteRef.getObjectId();

            if (localId == null) {
                return commits; // No local commits
            }

            // If local and remote are the same, no unpushed commits
            if (localId.equals(remoteId)) {
                return commits;
            }

            // Get commits between remote and local
            Iterable<RevCommit> unpushed = requireGit().log()
                    .addRange(remoteId, localId)
                    .call();

            for (RevCommit commit : unpushed) {
                commits.add(commit.getShortMessage());
            }

        } catch (Exception e) {
            // If we can't determine unpushed commits, show recent commits
            Iterable<RevCommit> logs = requireGit().log().setMaxCount(5).call();
            for (RevCommit commit : logs) {
                commits.add(commit.getShortMessage());
            }
        }

        return commits;
    }

    public List<String> getChangedFiles() throws GitAPIException {
        List<String> files = new ArrayList<>();
        Status status = requireGit().status().call();

        files.addAll(status.getAdded());
        files.addAll(status.getChanged());
        files.addAll(status.getModified());
        files.addAll(status.getRemoved());
        files.addAll(status.getMissing());
        files.addAll(status.getUntracked());

        return files;
    }

    public StatusInfo getDetailedStatus() throws GitAPIException {
        Status status = requireGit().status().call();
        return new StatusInfo(
                new ArrayList<>(status.getAdded()),
                new ArrayList<>(status.getModified()),
                new ArrayList<>(status.getChanged()),
                new ArrayList<>(status.getRemoved()),
                new ArrayList<>(status.getMissing()),
                new ArrayList<>(status.getUntracked()),
                new ArrayList<>(status.getConflicting())
        );
    }

    public String listBranches() throws GitAPIException {
        StringBuilder sb = new StringBuilder();
        List<Ref> branches = requireGit().branchList().call();
        String currentBranch = getCurrentBranch();

        for (Ref branch : branches) {
            String name = branch.getName().replace("refs/heads/", "");
            if (name.equals(currentBranch)) {
                sb.append("* ").append(name).append("\n");
            } else {
                sb.append("  ").append(name).append("\n");
            }
        }

        return sb.toString();
    }

    public void createBranch(String name) throws GitAPIException {
        requireGit().branchCreate().setName(name).call();
    }

    public void switchBranch(String name) throws GitAPIException {
        requireGit().checkout().setName(name).call();
    }

    public void deleteBranch(String name, boolean force) throws GitAPIException {
        requireGit().branchDelete()
                .setBranchNames(name)
                .setForce(force)
                .call();
    }

    public String getCommitHistory(int count) throws GitAPIException {
        StringBuilder sb = new StringBuilder();
        Iterable<RevCommit> logs = requireGit().log().setMaxCount(count).call();

        for (RevCommit commit : logs) {
            sb.append(commit.getId().abbreviate(7).name())
                    .append(" - ")
                    .append(commit.getShortMessage())
                    .append(" (")
                    .append(commit.getAuthorIdent().getName())
                    .append(")\n");
        }

        return sb.toString();
    }

    public void stashChanges() throws GitAPIException {
        requireGit().stashCreate().call();
    }

    public void stashPop() throws GitAPIException {
        requireGit().stashApply().call();
        requireGit().stashDrop().setStashRef(0).call();
    }

    public String listStashes() throws GitAPIException {
        StringBuilder sb = new StringBuilder();
        var stashes = requireGit().stashList().call();

        int index = 0;
        for (RevCommit stash : stashes) {
            sb.append("stash@{").append(index++).append("}: ")
                    .append(stash.getShortMessage()).append("\n");
        }

        if (index == 0) {
            sb.append("No stashes found\n");
        }

        return sb.toString();
    }

    public void undoLastCommit() throws GitAPIException {
        requireGit().reset().setMode(org.eclipse.jgit.api.ResetCommand.ResetType.SOFT)
                .setRef("HEAD~1").call();
    }

    public boolean hasConflicts() throws GitAPIException {
        Status status = requireGit().status().call();
        return !status.getConflicting().isEmpty();
    }

    public List<String> getConflictingFiles() throws GitAPIException {
        Status status = requireGit().status().call();
        return new ArrayList<>(status.getConflicting());
    }

    @Override
    public void close() {
        if (git != null) {
            git.close();
        }
        if (repository != null) {
            repository.close();
        }
    }

    // Public method to stage files
    public void stageFile(String filePattern) throws GitAPIException {
        requireGit().add().addFilepattern(filePattern).call();
    }

    // Inner class to hold detailed status information
    public static class StatusInfo {
        public final List<String> added;
        public final List<String> modified;
        public final List<String> changed;
        public final List<String> removed;
        public final List<String> missing;
        public final List<String> untracked;
        public final List<String> conflicting;

        public StatusInfo(List<String> added, List<String> modified, List<String> changed,
                          List<String> removed, List<String> missing, List<String> untracked,
                          List<String> conflicting) {
            this.added = added;
            this.modified = modified;
            this.changed = changed;
            this.removed = removed;
            this.missing = missing;
            this.untracked = untracked;
            this.conflicting = conflicting;
        }
    }
}
