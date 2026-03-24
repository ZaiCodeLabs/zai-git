---

## **README.md** (Production-Ready + Conversion-Optimized)

```markdown id="8z4m1p"
# ZaiGit - AI-Powered Git CLI

Beautiful Git workflows. Local AI. Minimal friction. Cross-platform.

[![Java 21](https://img.shields.io/badge/Java-21-blue.svg)](https://adoptium.net/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Version](https://img.shields.io/badge/Version-1.0.0-blue.svg)](https://github.com/ZaiCodeLabs/zai-git/releases)

```

╔═══════════════════════════════════════════════╗
║  ZaiGit - AI-Powered Git Made Simple          ║
║                                               ║
║  Smart Push • Local AI • Interactive CLI      ║
║  Sync • Branching • Fast                      ║
║                                               ║
║  cd repo && zai-git push                      ║
╚═══════════════════════════════════════════════╝

````

ZaiGit streamlines your Git workflow by combining automation, intelligent defaults, and optional AI-powered commit messages—all while keeping full control in your hands.

---

## Why ZaiGit?

- AI-generated commit messages (local, privacy-focused)
- Clean and interactive terminal experience
- Automated pull-before-push (configurable)
- Built-in conflict detection and resolution
- Full branch management support
- Stash, undo, and sync workflows
- Explicit confirmation before every push
- Works with GitHub, GitLab, and Bitbucket
- Lightweight Java-based CLI (runs anywhere)

---

## Installation (Under 1 Minute)

### Linux/macOS
```bash
wget https://github.com/ZaiCodeLabs/zai-git/releases/latest/download/install.sh
chmod +x install.sh
./install.sh
source ~/.bashrc
zai-git --version
````

### Build from Source

```bash
git clone https://github.com/ZaiCodeLabs/zai-git.git
cd zai-git
./install.sh --build
```

**Requirements:** Java 21+ and Git

---

## Quick Start

```bash
cd your/git/repo
zai-git status
echo "example change" >> file.txt
zai-git push
```

### One-Time Setup (Per Repository)

```bash
git remote set-url origin https://USER:TOKEN@github.com/ORG/REPO.git
```

---

## Core Commands

```bash
zai-git push        # Smart push with commit generation
zai-git sync        # Pull + push
zai-git status      # Repository overview
zai-git branches    # Branch management
zai-git log         # Commit history
zai-git stash       # Stash operations
zai-git undo        # Undo last commit
zai-git             # Interactive mode
```

---

## Example Output

```
Smart Push
═════════════
AI Generated: feat: add user authentication

File Changes:
  Added (2 files)
  Modified (1 file)

Push to origin/main? (Y/n):
```

---

## AI Features (Optional)

ZaiGit integrates with Ollama for local AI-powered commit messages.

```bash
curl -fsSL https://ollama.com/install.sh | sh
ollama serve &
ollama pull codellama
```

Configuration is available via:

```bash
zai-git settings
```

ZaiGit functions fully without AI if Ollama is not installed.

---

## Authentication

ZaiGit uses your existing Git configuration.

* HTTPS: `git remote set-url origin https://USER:TOKEN@...`
* SSH: Uses your existing SSH keys

No credentials are stored by ZaiGit.

---

## Platform Support

| Platform | Status    |
| -------- | --------- |
| Linux    | Supported |
| macOS    | Supported |
| Windows  | Supported |
| WSL      | Supported |

---

## Build

```bash
./mvnw clean package -DskipTests
```

---

## Use Cases

ZaiGit is suitable for:

* Individual developers
* Small teams
* Open-source maintainers
* Students learning Git
* Developers seeking a simpler Git workflow

---

## Recommended Workflow

```bash
# Start of day
zai-git sync

# Development
zai-git status
zai-git push

# End of day
zai-git sync
```

---

## Documentation

* INSTALLATION.md
* HELP.md
* TEST.md
* SECURITY.md

---

## Contributing

```bash
git clone https://github.com/ZaiCodeLabs/zai-git.git
cd zai-git
./mvnw clean package -DskipTests
./install.sh --build
```

See CONTRIBUTING.md for guidelines.

---

## License

MIT License

---

## About ZaiCode Labs

ZaiCode Labs is a South African software development startup focused on building practical, scalable tools for developers and businesses.

Location: South Africa

Website: [https://zaicodelabs.co.za](https://zaicodelabs.co.za)
Email: [info@zaicodelabs.co.za](mailto:info@zaicodelabs.co.za)

---

## Support

* GitHub Issues: [https://github.com/ZaiCodeLabs/zai-git/issues](https://github.com/ZaiCodeLabs/zai-git/issues)
* Discussions: [https://github.com/ZaiCodeLabs/zai-git/discussions](https://github.com/ZaiCodeLabs/zai-git/discussions)

---