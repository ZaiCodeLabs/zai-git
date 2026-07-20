package za.co.zaicodelabs.zaigit.cli;

import org.springframework.stereotype.Component;
import za.co.zaicodelabs.zaigit.service.*;
import za.co.zaicodelabs.zaigit.service.GitService.StatusInfo;

import java.util.Scanner;

@Component
public class GitCLIController {

    private final GitService gitService;
    private final OllamaService ollamaService;
    private final UIService uiService;
    private final ConfigService configService;
    private final ConflictResolverService conflictResolverService;
    private final Scanner scanner;

    public GitCLIController(GitService gitService, OllamaService ollamaService,
                            UIService uiService, ConfigService configService,
                            ConflictResolverService conflictResolverService) {
        this.gitService = gitService;
        this.ollamaService = ollamaService;
        this.uiService = uiService;
        this.configService = configService;
        this.conflictResolverService = conflictResolverService;
        this.scanner = new Scanner(System.in);
    }

    public int run(String[] args) {
        try {
            if (args.length == 0) {
                return runInteractiveMode();
            }
            return runCommandMode(args);
        } catch (Exception e) {
            uiService.error("Error: " + e.getMessage());
            return 1;
        }
    }

    private int runInteractiveMode() {
        uiService.printBanner();

        while (true) {
            uiService.printMainMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> smartPush();
                case "2" -> smartPull();
                case "3" -> showStatus();
                case "4" -> branchOperations();
                case "5" -> showCommitHistory();
                case "6" -> stashManagement();
                case "7" -> undoLastCommit();
                case "8" -> syncRepository();
                case "9" -> settings();
                case "0" -> {
                    uiService.success("Thank you for using ZaiGit!");
                    return 0;
                }
                default -> uiService.warning("Invalid option. Please try again.");
            }
        }
    }

    private int runCommandMode(String[] args) {
        String command = args[0].toLowerCase();

        try {
            switch (command) {
                case "push" -> {
                    if (args.length > 2 && "-m".equals(args[1])) {
                        String message = args[2];
                        if (gitService.hasChanges()) {
                            gitService.commit(message);
                        }
                        gitService.push();
                        uiService.success(" Changes pushed successfully!");
                    } else {
                        return smartPush() ? 0 : 1;
                    }
                }
                case "pull" -> { return smartPull() ? 0 : 1; }
                case "status" -> { return showStatus() ? 0 : 1; }
                case "sync" -> { return syncRepository() ? 0 : 1; }
                case "log" -> { return showCommitHistory() ? 0 : 1; }
                case "stash" -> {
                    if (args.length > 1 && "pop".equals(args[1])) {
                        gitService.stashPop();
                        uiService.success(" Stash applied");
                    } else {
                        gitService.stashChanges();
                        uiService.success(" Changes stashed");
                    }
                }
                case "undo" -> undoLastCommit();
                case "conflicts" -> conflictResolverService.resolveAllConflicts();
                case "help", "--help", "-h" -> printHelp();
                case "version", "--version", "-v" -> uiService.info("ZaiGit v" + applicationVersion());
                default -> {
                    uiService.error("Unknown command: " + command);
                    printHelp();
                    return 2;
                }
            }
            return 0;
        } catch (Exception e) {
            uiService.error("Command failed: " + e.getMessage());
            return 1;
        }
    }

    private boolean smartPush() {
        uiService.section("Smart Push");

        try {
            // Check for conflicts first
            if (conflictResolverService.hasConflicts()) {
                uiService.error("Cannot push: conflicts detected");
                uiService.info("Resolve conflicts first using option 8 or 'zai-git conflicts'");
                return false;
            }

            // Auto-pull first if enabled
            if (configService.isAutoPullBeforePush()) {
                uiService.progress("Pulling from remote...");
                pullPreservingLocalChanges();
                uiService.clearProgress();
                uiService.success(" Pull completed");

                // Check for conflicts after pull
                if (conflictResolverService.hasConflicts()) {
                    uiService.error("Conflicts detected after pull");
                    uiService.info("Resolve conflicts? (y/N): ");
                    String resolve = scanner.nextLine().trim().toLowerCase();
                    if (resolve.equals("y") || resolve.equals("yes")) {
                        conflictResolverService.resolveAllConflicts();
                    } else {
                        return false;
                    }
                }
            }

            boolean hasChanges = gitService.hasChanges();
            var unpushedCommits = gitService.getUnpushedCommits();

            if (!hasChanges && unpushedCommits.isEmpty()) {
                uiService.success("Repository is up to date!");
                return true;
            }

            // Generate AI commit message
            String commitMessage = null;
            if (hasChanges) {
                if (configService.isAiCommitMessagesEnabled() && ollamaService.isAvailable()) {
                    uiService.progress("Analyzing changes with AI...");
                    String diff = gitService.getDiff();
                    commitMessage = ollamaService.generateCommitMessage(diff);
                    uiService.clearProgress();
                    uiService.success("AI Generated: " + commitMessage);
                } else {
                    uiService.info("Enter commit message:");
                    commitMessage = scanner.nextLine().trim();
                    if (commitMessage.isEmpty()) {
                        uiService.warning("Commit message cannot be empty");
                        return false;
                    }
                }
            }

            // Show changes to be pushed
            StatusInfo statusInfo = gitService.getDetailedStatus();
            uiService.printPushPreviewDetailed(
                    unpushedCommits,
                    statusInfo
            );

            // Confirm push if enabled
            if (configService.isConfirmBeforePush()) {
                uiService.info("Push to remote? (Y/n): ");
                String confirm = scanner.nextLine().trim().toLowerCase();

                if (!confirm.isEmpty() && !confirm.equals("y") && !confirm.equals("yes")) {
                    uiService.warning("Push cancelled");
                    return false;
                }
            }

            uiService.progress("Pushing changes...");
            if (hasChanges) {
                gitService.commit(commitMessage);
            }
            gitService.push();
            uiService.clearProgress();
            uiService.success(" Push completed successfully!");
            return true;

        } catch (Exception e) {
            uiService.clearProgress();
            uiService.error("Push failed: " + e.getMessage());
            return false;
        }
    }

    private boolean smartPull() {
        uiService.section("Smart Pull");

        try {
            uiService.progress("Pulling from remote...");
            pullPreservingLocalChanges();
            uiService.clearProgress();
            uiService.success("Pull completed successfully!");

            // Check for conflicts
            if (conflictResolverService.hasConflicts()) {
                uiService.error("Conflicts detected after pull");
                uiService.info("Resolve conflicts? (y/N): ");
                String resolve = scanner.nextLine().trim().toLowerCase();
                if (resolve.equals("y") || resolve.equals("yes")) {
                    conflictResolverService.resolveAllConflicts();
                }
            }
            return true;
        } catch (Exception e) {
            uiService.clearProgress();
            uiService.error("Pull failed: " + e.getMessage());
            return false;
        }
    }

    private boolean showStatus() {
        uiService.section("Repository Status");

        try {
            String status = gitService.getStatus();
            System.out.println(status);

            // Show if there are unpushed commits
            var unpushed = gitService.getUnpushedCommits();
            if (!unpushed.isEmpty()) {
                System.out.println("\nUnpushed commits: " + unpushed.size());
            }
            return true;
        } catch (Exception e) {
            uiService.error("Failed to get status: " + e.getMessage());
            return false;
        }
    }

    private void branchOperations() {
        uiService.section("Branch Operations");
        System.out.println("1. List Branches");
        System.out.println("2. Create Branch");
        System.out.println("3. Switch Branch");
        System.out.println("4. Delete Branch");
        System.out.println("0. Back");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> {
                try {
                    System.out.println(gitService.listBranches());
                } catch (Exception e) {
                    uiService.error("Failed: " + e.getMessage());
                }
            }
            case "2" -> {
                System.out.print("Branch name: ");
                String name = scanner.nextLine().trim();
                if (name.isEmpty()) {
                    uiService.error("Branch name cannot be empty");
                    return;
                }
                try {
                    gitService.createBranch(name);
                    uiService.success("Branch created: " + name);
                } catch (Exception e) {
                    uiService.error("Failed: " + e.getMessage());
                }
            }
            case "3" -> {
                System.out.print("Branch name: ");
                String name = scanner.nextLine().trim();
                if (name.isEmpty()) {
                    uiService.error("Branch name cannot be empty");
                    return;
                }
                try {
                    gitService.switchBranch(name);
                    uiService.success("Switched to: " + name);
                } catch (Exception e) {
                    uiService.error("Failed: " + e.getMessage());
                }
            }
            case "4" -> {
                System.out.print("Branch name: ");
                String name = scanner.nextLine().trim();
                if (name.isEmpty()) {
                    uiService.error("Branch name cannot be empty");
                    return;
                }

                System.out.print("Force delete (unmerged changes will be lost)? (y/N): ");
                String forceStr = scanner.nextLine().trim().toLowerCase();
                boolean force = forceStr.equals("y") || forceStr.equals("yes");

                try {
                    gitService.deleteBranch(name, force);
                    uiService.success("Branch deleted: " + name);
                } catch (Exception e) {
                    uiService.error("Failed: " + e.getMessage());
                    if (!force) {
                        uiService.info("Tip: Use force delete if branch has unmerged changes");
                    }
                }
            }
            case "0" -> {}
            default -> uiService.warning("Invalid option.");
        }
    }

    private boolean showCommitHistory() {
        uiService.section("Commit History");

        try {
            String history = gitService.getCommitHistory(10);
            System.out.println(history);
            return true;
        } catch (Exception e) {
            uiService.error("Failed: " + e.getMessage());
            return false;
        }
    }

    private void stashManagement() {
        uiService.section("Stash Management");
        System.out.println("1. Stash Changes");
        System.out.println("2. Apply Stash");
        System.out.println("3. List Stashes");
        System.out.println("0. Back");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> {
                try {
                    gitService.stashChanges();
                    uiService.success(" Changes stashed");
                } catch (Exception e) {
                    uiService.error("Failed: " + e.getMessage());
                }
            }
            case "2" -> {
                try {
                    gitService.stashPop();
                    uiService.success(" Stash applied");
                } catch (Exception e) {
                    uiService.error("Failed: " + e.getMessage());
                }
            }
            case "3" -> {
                try {
                    System.out.println(gitService.listStashes());
                } catch (Exception e) {
                    uiService.error("Failed: " + e.getMessage());
                }
            }
            case "0" -> {}
            default -> uiService.warning("Invalid option.");
        }
    }

    private void undoLastCommit() {
        uiService.section("Undo Last Commit");
        uiService.warning("This will undo the last commit (keeping changes). Continue? (y/N): ");

        String confirm = scanner.nextLine().trim().toLowerCase();
        if (confirm.equals("y") || confirm.equals("yes")) {
            try {
                gitService.undoLastCommit();
                uiService.success(" Last commit undone");
            } catch (Exception e) {
                uiService.error("Failed: " + e.getMessage());
            }
        } else {
            uiService.info("Cancelled.");
        }
    }

    private boolean syncRepository() {
        uiService.section("Sync Repository");

        try {
            uiService.progress("Pulling...");
            pullPreservingLocalChanges();
            uiService.clearProgress();

            // Check for conflicts
            if (conflictResolverService.hasConflicts()) {
                uiService.error("Conflicts detected");
                conflictResolverService.resolveAllConflicts();
            }

            if (gitService.hasChanges()) {
                return smartPush();
            } else {
                uiService.success("Repository is up to date!");
            }
            return true;
        } catch (Exception e) {
            uiService.clearProgress();
            uiService.error("Sync failed: " + e.getMessage());
            return false;
        }
    }

    private void pullPreservingLocalChanges() throws Exception {
        boolean stashCreated = false;

        if (gitService.hasChanges()) {
            stashCreated = gitService.stashChanges();
        }

        try {
            gitService.pull();
        } catch (Exception pullFailure) {
            if (stashCreated) {
                try {
                    gitService.stashPop();
                } catch (Exception restoreFailure) {
                    throw new IllegalStateException(
                            pullFailure.getMessage()
                                    + "; local changes remain safely stored in the stash because they could not be restored: "
                                    + restoreFailure.getMessage(),
                            pullFailure);
                }
            }
            throw pullFailure;
        }

        if (stashCreated) {
            try {
                gitService.stashPop();
            } catch (Exception restoreFailure) {
                throw new IllegalStateException(
                        "Pull completed, but local changes could not be restored automatically. "
                                + "They remain safely stored in the stash: " + restoreFailure.getMessage(),
                        restoreFailure);
            }
        }
    }

    private void settings() {
        uiService.section("Settings");
        System.out.println("1. Configure Ollama");
        System.out.println("2. Test Ollama Connection");
        System.out.println("3. View Configuration");
        System.out.println("4. Toggle Features");
        System.out.println("0. Back");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> configureOllama();
            case "2" -> testOllamaConnection();
            case "3" -> viewConfiguration();
            case "4" -> toggleFeatures();
            case "0" -> {}
            default -> uiService.warning("Invalid option.");
        }
    }

    private void configureOllama() {
        System.out.print("Ollama URL (current: " + configService.getOllamaUrl() + "): ");
        String url = scanner.nextLine().trim();
        if (!url.isEmpty()) {
            configService.setOllamaUrl(url);
        }

        System.out.print("Model (current: " + configService.getOllamaModel() + "): ");
        String model = scanner.nextLine().trim();
        if (!model.isEmpty()) {
            configService.setOllamaModel(model);
        }

        configService.saveConfig();
        uiService.success(" Configuration saved");
    }

    private void testOllamaConnection() {
        uiService.progress("Testing Ollama connection...");
        boolean available = ollamaService.isAvailable();
        uiService.clearProgress();

        if (available) {
            uiService.success(" Ollama is available at " + configService.getOllamaUrl());
        } else {
            uiService.error("Ollama is not available");
            uiService.info("Make sure Ollama is running: ollama serve");
            uiService.info("Or check URL in settings: " + configService.getOllamaUrl());
        }
    }

    private void viewConfiguration() {
        uiService.info("Current Configuration:");
        System.out.println("\n Git Repository: " + System.getProperty("user.dir"));
        System.out.println(" Current Branch: " + gitService.getCurrentBranch());
        System.out.println("\n Ollama:");
        System.out.println("   URL: " + configService.getOllamaUrl());
        System.out.println("   Model: " + configService.getOllamaModel());
        System.out.println("   Status: " + (ollamaService.isAvailable() ? "Connected " : "Not available"));
        System.out.println("\n  Features:");
        System.out.println("   AI Commit Messages: " + (configService.isAiCommitMessagesEnabled() ? "Enabled" : "Disabled"));
        System.out.println("   Push Confirmation: " + (configService.isConfirmBeforePush() ? "Enabled" : "Disabled"));
        System.out.println("   Auto-pull Before Push: " + (configService.isAutoPullBeforePush() ? "Enabled" : "Disabled"));
        System.out.println("\n Config File: " + configService.getConfigFilePath());
        System.out.println(" Java Version: " + System.getProperty("java.version"));
    }

    private void toggleFeatures() {
        System.out.println("\n1. Toggle AI Commit Messages (current: " +
                (configService.isAiCommitMessagesEnabled() ? "ON" : "OFF") + ")");
        System.out.println("2. Toggle Push Confirmation (current: " +
                (configService.isConfirmBeforePush() ? "ON" : "OFF") + ")");
        System.out.println("3. Toggle Auto-pull Before Push (current: " +
                (configService.isAutoPullBeforePush() ? "ON" : "OFF") + ")");
        System.out.println("0. Back");

        System.out.print("\nChoose option: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> {
                configService.setAiCommitMessagesEnabled(!configService.isAiCommitMessagesEnabled());
                configService.saveConfig();
                uiService.success("AI Commit Messages: " +
                        (configService.isAiCommitMessagesEnabled() ? "ON" : "OFF"));
            }
            case "2" -> {
                configService.setConfirmBeforePush(!configService.isConfirmBeforePush());
                configService.saveConfig();
                uiService.success("Push Confirmation: " +
                        (configService.isConfirmBeforePush() ? "ON" : "OFF"));
            }
            case "3" -> {
                configService.setAutoPullBeforePush(!configService.isAutoPullBeforePush());
                configService.saveConfig();
                uiService.success("Auto-pull Before Push: " +
                        (configService.isAutoPullBeforePush() ? "ON" : "OFF"));
            }
            case "0" -> {}
            default -> uiService.warning("Invalid option");
        }
    }

    private void printHelp() {
        System.out.println("""
            
            ZaiGit - AI-Powered Git Automation
            
            Usage:
              zai-git                    Interactive mode
              zai-git push               Smart push
              zai-git push -m "message"  Push with message
              zai-git pull               Smart pull
              zai-git status             Show status
              zai-git sync               Sync repository
              zai-git log                Show commit history
              zai-git stash              Stash changes
              zai-git stash pop          Apply stash
              zai-git undo               Undo last commit
              zai-git conflicts          Resolve conflicts
              zai-git help               Show this help
              zai-git version            Show version
            
            Developed by ZaiCode Labs
            https://zaicodelabs.co.za
            """);
    }

    private String applicationVersion() {
        String version = GitCLIController.class.getPackage().getImplementationVersion();
        return version == null ? "development" : version;
    }
}
