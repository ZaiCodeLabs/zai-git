package za.co.zaicodelabs.zaigit.service;

import org.springframework.stereotype.Service;
import za.co.zaicodelabs.zaigit.service.GitService.StatusInfo;
import java.util.List;

@Service
public class UIService {

    // ZaiCode Labs Brand Colors
    static final String PRIMARY = "\u001B[38;2;61;74;75m";      // #3d4a4b
    static final String ACCENT = "\u001B[38;2;0;234;250m";      // #00eafa (cyan)
    static final String RESET = "\u001B[0m";
    static final String BOLD = "\u001B[1m";
    static final String GREEN = "\u001B[32m";
    static final String YELLOW = "\u001B[33m";
    static final String RED = "\u001B[31m";
    static final String BLUE = "\u001B[34m";

    public void printBanner() {
        String banner = PRIMARY + """
        
        ╔═══════════════════════════════════════════════════════════════════╗
        ║                                                                   ║
        ║   ███████╗ █████╗ ██╗     ██████╗ ██╗████████╗                    ║
        ║   ╚══███╔╝██╔══██╗██║    ██╔════╝ ██║╚══██╔══╝                    ║
        ║     ███╔╝ ███████║██║    ██║  ███╗██║   ██║                       ║
        ║    ███╔╝  ██╔══██║██║    ██║   ██║██║   ██║                       ║
        ║   ███████╗██║  ██║██║    ╚██████╔╝██║   ██║                       ║
        ║   ╚══════╝╚═╝  ╚═╝╚═╝     ╚═════╝ ╚═╝   ╚═╝                       ║
        ║                                                                   ║
        ║    AI-Powered Git Automation                                      ║
        ║    Smart Workflow | Auto Commits | Zero Hassle                    ║
        ║                                                                   ║
        ║   Built with %s by ZaiCode Labs                                   ║
        ║   https://zaicodelabs.co.za                                       ║
        ║                                                                   ║
        ╚═══════════════════════════════════════════════════════════════════╝
        """;
        System.out.println(String.format(banner, ACCENT + "❤ " + PRIMARY) + RESET);
    }

    public void printMainMenu() {
        System.out.println(PRIMARY + "\n╔════════════ MAIN MENU ════════════╗" + RESET);
        System.out.println("  1.  Smart Push");
        System.out.println("  2.  Smart Pull");
        System.out.println("  3.  Status");
        System.out.println("  4.  Branch Operations");
        System.out.println("  5.  Commit History");
        System.out.println("  6.  Stash Management");
        System.out.println("  7.️ Undo Last Commit");
        System.out.println("  8.  Sync Repository");
        System.out.println("  9.  Settings");
        System.out.println("  0.  Exit");
        System.out.println(PRIMARY + "╚═══════════════════════════════════╝" + RESET);
        System.out.print(ACCENT + "\n→ Choose option: " + RESET);
    }

    public void printPushPreview(List<String> commits, List<String> files) {
        System.out.println("\n" + PRIMARY + "╔════════════ COMMITS TO PUSH ════════════╗" + RESET);

        if (commits.isEmpty()) {
            System.out.println("  No unpushed commits");
        } else {
            for (String commit : commits) {
                System.out.println("  " + commit);
            }
        }

        System.out.println(PRIMARY + "╚═════════════════════════════════════════╝" + RESET);

        System.out.println("\n" + PRIMARY + " File Changes:" + RESET);

        if (files.isEmpty()) {
            System.out.println("  No changes");
        } else {
            System.out.println("\n   " + YELLOW + " Changed (" + files.size() + " files):" + RESET);
            files.forEach(f -> System.out.println("      • " + f));
            System.out.println("\n   Total: " + files.size() + " change(s)");
        }

        System.out.println();
    }

    public void printPushPreviewDetailed(List<String> commits, StatusInfo statusInfo) {
        System.out.println("\n" + PRIMARY + "╔════════════ COMMITS TO PUSH ════════════╗" + RESET);

        if (commits.isEmpty()) {
            System.out.println("  No unpushed commits");
        } else {
            for (String commit : commits) {
                System.out.println(" " + commit);
            }
        }

        System.out.println(PRIMARY + "╚═════════════════════════════════════════╝" + RESET);

        System.out.println("\n" + PRIMARY + " File Changes:" + RESET);

        int totalChanges = statusInfo.added.size() + statusInfo.modified.size() +
                statusInfo.changed.size() + statusInfo.removed.size() +
                statusInfo.missing.size() + statusInfo.untracked.size();

        if (totalChanges == 0) {
            System.out.println("  No changes");
        } else {
            if (!statusInfo.added.isEmpty()) {
                System.out.println("\n   " + GREEN + "➕ Added (" + statusInfo.added.size() + " files):" + RESET);
                statusInfo.added.forEach(f -> System.out.println("      • " + f));
            }

            if (!statusInfo.untracked.isEmpty()) {
                System.out.println("\n   " + GREEN + "➕ Untracked (" + statusInfo.untracked.size() + " files):" + RESET);
                statusInfo.untracked.forEach(f -> System.out.println("      • " + f));
            }

            if (!statusInfo.modified.isEmpty() || !statusInfo.changed.isEmpty()) {
                int modCount = statusInfo.modified.size() + statusInfo.changed.size();
                System.out.println("\n   " + YELLOW + " Modified (" + modCount + " files):" + RESET);
                statusInfo.modified.forEach(f -> System.out.println("      • " + f));
                statusInfo.changed.forEach(f -> System.out.println("      • " + f));
            }

            if (!statusInfo.removed.isEmpty() || !statusInfo.missing.isEmpty()) {
                int removedCount = statusInfo.removed.size() + statusInfo.missing.size();
                System.out.println("\n   " + RED + "➖ Removed (" + removedCount + " files):" + RESET);
                statusInfo.removed.forEach(f -> System.out.println("      • " + f));
                statusInfo.missing.forEach(f -> System.out.println("      • " + f));
            }

            if (!statusInfo.conflicting.isEmpty()) {
                System.out.println("\n   " + RED + " Conflicting (" + statusInfo.conflicting.size() + " files):" + RESET);
                statusInfo.conflicting.forEach(f -> System.out.println("      • " + f));
            }

            System.out.println("\n   Total: " + totalChanges + " change(s)");
        }

        System.out.println();
    }

    public void section(String title) {
        System.out.println("\n" + PRIMARY + BOLD + "→ " + title + RESET);
        System.out.println(PRIMARY + "═".repeat(title.length() + 2) + RESET);
    }

    public void success(String message) {
        System.out.println(GREEN + "✓ " + message + RESET);
    }

    public void error(String message) {
        System.out.println(RED + "✗ " + message + RESET);
    }

    public void warning(String message) {
        System.out.println(YELLOW + " " + message + RESET);
    }

    public void info(String message) {
        System.out.println(BLUE + "ℹ " + message + RESET);
    }

    public void progress(String message) {
        System.out.print(ACCENT + "⟳ " + message + RESET + "\r");
        System.out.flush();
    }

    public void clearProgress() {
        System.out.print("\r" + " ".repeat(80) + "\r");
        System.out.flush();
    }
}
