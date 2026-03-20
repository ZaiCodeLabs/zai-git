package za.co.zaicodelabs.zaigit.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

@Service
public class ConflictResolverService {

    private final OllamaService ollamaService;
    private final UIService uiService;
    private final GitService gitService;
    private final Scanner scanner;

    public ConflictResolverService(OllamaService ollamaService, UIService uiService, GitService gitService) {
        this.ollamaService = ollamaService;
        this.uiService = uiService;
        this.gitService = gitService;
        this.scanner = new Scanner(System.in);
    }

    public void resolveConflict(String conflictFile, String conflictContent) {
        uiService.section("Conflict Resolution: " + conflictFile);

        // Show conflict
        System.out.println("\n" + UIService.RED + "Conflict detected in file:" + UIService.RESET);
        System.out.println(conflictContent);

        // Get AI explanation if available
        if (ollamaService.isAvailable()) {
            uiService.progress("Getting AI analysis...");
            String explanation = ollamaService.explainConflict(conflictContent);
            uiService.clearProgress();
            uiService.info("\n💡 AI Analysis:");
            System.out.println(explanation);
            System.out.println();
        }

        // Provide resolution options
        System.out.println("\nResolution options:");
        System.out.println("1. Keep current changes (ours)");
        System.out.println("2. Accept incoming changes (theirs)");
        System.out.println("3. Manually edit file");
        System.out.println("4. Skip this file");

        System.out.print("\nChoose option (1-4): ");
        String choice = scanner.nextLine().trim();

        try {
            Path filePath = Paths.get(conflictFile);

            switch (choice) {
                case "1" -> {
                    // Keep ours - extract content between <<<<<<< and =======
                    String resolved = resolveKeepOurs(conflictContent);
                    Files.writeString(filePath, resolved);
                    uiService.success("✓ Kept current changes");
                }
                case "2" -> {
                    // Keep theirs - extract content between ======= and >>>>>>>
                    String resolved = resolveKeepTheirs(conflictContent);
                    Files.writeString(filePath, resolved);
                    uiService.success("✓ Accepted incoming changes");
                }
                case "3" -> {
                    uiService.info("Please edit the file manually:");
                    uiService.info("  File: " + conflictFile);
                    uiService.info("  Remove conflict markers: <<<<<<<, =======, >>>>>>>");
                    uiService.info("\nPress Enter when done...");
                    scanner.nextLine();
                    uiService.success("✓ Manual edit completed");
                }
                case "4" -> {
                    uiService.warning("⚠ Skipped file");
                }
                default -> {
                    uiService.error("✗ Invalid option");
                }
            }
        } catch (IOException e) {
            uiService.error("✗ Failed to resolve conflict: " + e.getMessage());
        }
    }

    private String resolveKeepOurs(String conflictContent) {
        StringBuilder resolved = new StringBuilder();
        String[] lines = conflictContent.split("\n");
        boolean inOurs = false;
        boolean inTheirs = false;

        for (String line : lines) {
            if (line.startsWith("<<<<<<<")) {
                inOurs = true;
                continue;
            }
            if (line.startsWith("=======")) {
                inOurs = false;
                inTheirs = true;
                continue;
            }
            if (line.startsWith(">>>>>>>")) {
                inTheirs = false;
                continue;
            }

            if (inOurs || (!inOurs && !inTheirs)) {
                resolved.append(line).append("\n");
            }
        }

        return resolved.toString();
    }

    private String resolveKeepTheirs(String conflictContent) {
        StringBuilder resolved = new StringBuilder();
        String[] lines = conflictContent.split("\n");
        boolean inOurs = false;
        boolean inTheirs = false;

        for (String line : lines) {
            if (line.startsWith("<<<<<<<")) {
                inOurs = true;
                continue;
            }
            if (line.startsWith("=======")) {
                inOurs = false;
                inTheirs = true;
                continue;
            }
            if (line.startsWith(">>>>>>>")) {
                inTheirs = false;
                continue;
            }

            if (inTheirs || (!inOurs && !inTheirs)) {
                resolved.append(line).append("\n");
            }
        }

        return resolved.toString();
    }

    public boolean hasConflicts() {
        try {
            return gitService.hasConflicts();
        } catch (Exception e) {
            return false;
        }
    }

    public void resolveAllConflicts() {
        try {
            List<String> conflictingFiles = gitService.getConflictingFiles();

            if (conflictingFiles.isEmpty()) {
                uiService.info("No conflicts to resolve");
                return;
            }

            uiService.warning("⚠  Found " + conflictingFiles.size() + " file(s) with conflicts");

            for (String file : conflictingFiles) {
                try {
                    String content = Files.readString(Paths.get(file));
                    resolveConflict(file, content);
                } catch (IOException e) {
                    uiService.error("✗ Failed to read file: " + file);
                }
            }

            // After resolving, add the files
            uiService.info("\nAdd resolved files? (y/N): ");
            String confirm = scanner.nextLine().trim().toLowerCase();

            if (confirm.equals("y") || confirm.equals("yes")) {
                for (String file : conflictingFiles) {
                    gitService.stageFile(file);
                }
                uiService.success("✓ Resolved files added");
            }

        } catch (Exception e) {
            uiService.error("✗ Failed to resolve conflicts: " + e.getMessage());
        }
    }
}