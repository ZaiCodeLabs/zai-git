package za.co.zaicodelabs.zaigit.service;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GitService {

    private Git git;
    private Repository repository;

    public GitService() {
        try {
            initializeRepository();
        } catch (Exception e) {
            System.err.println("Warning: Not in a git repository. Some features may not work.");
        }
    }

    private void initializeRepository() throws IOException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        repository = builder
                .setGitDir(new File(".git"))
                .readEnvironment()
                .findGitDir()
                .build();
        git = new Git(repository);
    }

    public void pull() throws GitAPIException {
        git.pull().call();
    }

    public void push() throws GitAPIException {
        git.push().call();
    }

    public void commitAndPush(String message) throws GitAPIException {
        // Add all changes
        git.add().addFilepattern(".").call();

        // Also add removed files
        git.add().addFilepattern(".").setUpdate(true).call();

        // Commit
        git.commit().setMessage(message).call();

        // Push
        git.push().call();
    }

    public boolean hasChanges() throws GitAPIException {
        Status status = git.status().call();
        return status.hasUncommittedChanges() || !status.getUntracked().isEmpty();
    }

    public String getStatus() throws GitAPIException {
        Status status = git.status().call();
        StringBuilder sb = new StringBuilder();

        sb.append("Branch: ").append(getCurrentBranch()).append("\n\n");

        if (status.getAdded().isEmpty() && status.getChanged().isEmpty() &&
                status.getRemoved().isEmpty() && status.getUntracked().isEmpty() &&
                status.getModified().isEmpty()) {
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
            if (!status.getUntracked().isEmpty()) {
                sb.append("Untracked:\n");
                status.getUntracked().forEach(f -> sb.append("  ? ").append(f).append("\n"));
            }
        }

        return sb.toString();
    }

    public String getCurrentBranch() {
        try {
            return repository.getBranch();
        } catch (IOException e) {
            return "unknown";
        }
    }

    public String getDiff() throws GitAPIException, IOException {
        StringBuilder diff = new StringBuilder();
        Status status = git.status().call();

        // Get all changed files
        List<String> allChanges = new ArrayList<>();
        allChanges.addAll(status.getAdded());
        allChanges.addAll(status.getChanged());
        allChanges.addAll(status.getModified());
        allChanges.addAll(status.getRemoved());
        allChanges.addAll(status.getUntracked());

        if (allChanges.isEmpty()) {
            return "No changes";
        }

        // Build a summary of changes for AI
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

        if (!status.getRemoved().isEmpty()) {
            diff.append("\nFiles removed:\n");
            status.getRemoved().forEach(f -> diff.append("  - ").append(f).append("\n"));
        }

        diff.append("\nTotal changes: ").append(allChanges.size()).append(" file(s)");

        return diff.toString();
    }

    public List<String> getUnpushedCommits() throws GitAPIException, IOException {
        List<String> commits = new ArrayList<>();

        try {
            String currentBranch = getCurrentBranch();
            String remoteBranch = "origin/" + currentBranch;

            // Try to get remote ref
            Ref remoteRef = repository.findRef(remoteBranch);

            if (remoteRef == null) {
                // No remote tracking branch, return recent commits
                Iterable<RevCommit> logs = git.log().setMaxCount(5).call();
                for (RevCommit commit : logs) {
                    commits.add(commit.getShortMessage());
                }
                return commits;
            }

            // Get commits that are in local but not in remote
            ObjectId localId = repository.resolve(currentBranch);
            ObjectId remoteId = remoteRef.getObjectId();

            if (localId == null) {
                return commits; // No local commits
            }

            // If local and remote are the same, no unpushed commits
            if (localId.equals(remoteId)) {
                return commits;
            }

            // Get commits between remote and local
            Iterable<RevCommit> unpushed = git.log()
                    .addRange(remoteId, localId)
                    .call();

            for (RevCommit commit : unpushed) {
                commits.add(commit.getShortMessage());
            }

        } catch (Exception e) {
            // If we can't determine unpushed commits, show recent commits
            Iterable<RevCommit> logs = git.log().setMaxCount(5).call();
            for (RevCommit commit : logs) {
                commits.add(commit.getShortMessage());
            }
        }

        return commits;
    }

    public List<String> getChangedFiles() throws GitAPIException {
        List<String> files = new ArrayList<>();
        Status status = git.status().call();

        files.addAll(status.getAdded());
        files.addAll(status.getChanged());
        files.addAll(status.getModified());
        files.addAll(status.getRemoved());
        files.addAll(status.getUntracked());

        return files;
    }

    public StatusInfo getDetailedStatus() throws GitAPIException {
        Status status = git.status().call();
        return new StatusInfo(
                new ArrayList<>(status.getAdded()),
                new ArrayList<>(status.getModified()),
                new ArrayList<>(status.getChanged()),
                new ArrayList<>(status.getRemoved()),
                new ArrayList<>(status.getUntracked()),
                new ArrayList<>(status.getConflicting())
        );
    }

    public String listBranches() throws GitAPIException {
        StringBuilder sb = new StringBuilder();
        List<Ref> branches = git.branchList().call();
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
        git.branchCreate().setName(name).call();
    }

    public void switchBranch(String name) throws GitAPIException {
        git.checkout().setName(name).call();
    }

    public void deleteBranch(String name, boolean force) throws GitAPIException {
        git.branchDelete()
                .setBranchNames(name)
                .setForce(force)
                .call();
    }

    public String getCommitHistory(int count) throws GitAPIException {
        StringBuilder sb = new StringBuilder();
        Iterable<RevCommit> logs = git.log().setMaxCount(count).call();

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
        git.stashCreate().call();
    }

    public void stashPop() throws GitAPIException {
        git.stashApply().call();
    }

    public String listStashes() throws GitAPIException {
        StringBuilder sb = new StringBuilder();
        var stashes = git.stashList().call();

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
        git.reset().setMode(org.eclipse.jgit.api.ResetCommand.ResetType.SOFT)
                .setRef("HEAD~1").call();
    }

    public boolean hasConflicts() throws GitAPIException {
        Status status = git.status().call();
        return !status.getConflicting().isEmpty();
    }

    public List<String> getConflictingFiles() throws GitAPIException {
        Status status = git.status().call();
        return new ArrayList<>(status.getConflicting());
    }

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
        git.add().addFilepattern(filePattern).call();
    }

    // Inner class to hold detailed status information
    public static class StatusInfo {
        public final List<String> added;
        public final List<String> modified;
        public final List<String> changed;
        public final List<String> removed;
        public final List<String> untracked;
        public final List<String> conflicting;

        public StatusInfo(List<String> added, List<String> modified, List<String> changed,
                          List<String> removed, List<String> untracked, List<String> conflicting) {
            this.added = added;
            this.modified = modified;
            this.changed = changed;
            this.removed = removed;
            this.untracked = untracked;
            this.conflicting = conflicting;
        }
    }
}