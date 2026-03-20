# ZaiGit - AI-Powered Git Automation 🚀

> **By ZaiCode Labs** - Technology that makes life better

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![JGit](https://img.shields.io/badge/JGit-6.7.0-blue.svg)](https://www.eclipse.org/jgit/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Version](https://img.shields.io/badge/Version-1.0.0-00eafa.svg)](https://github.com/ZaiCodeLabs/zai-git/releases)

```
╔═══════════════════════════════════════════════════════════════════╗
║                                                                   ║
║   ███████╗ █████╗ ██╗     ██████╗ ██╗████████╗                   ║
║   ╚══███╔╝██╔══██╗██║    ██╔════╝ ██║╚══██╔══╝                   ║
║     ███╔╝ ███████║██║    ██║  ███╗██║   ██║                      ║
║    ███╔╝  ██╔══██║██║    ██║   ██║██║   ██║                      ║
║   ███████╗██║  ██║██║    ╚██████╔╝██║   ██║                      ║
║                                                                   ║
║   🤖 AI-Powered Git Automation                                   ║
║   🎯 Smart Workflow | Auto Commits | Zero Hassle                 ║
║                                                                   ║
║   Built with ❤️  by ZaiCode Labs                                 ║
║   Visit: https://zaicodelabs.co.za                               ║
╚═══════════════════════════════════════════════════════════════════╝
```

##  What is ZaiGit?

ZaiGit is an intelligent Git automation tool that combines AI-powered features with smart workflows to streamline your development process. Built with **Java 21** and **Spring Boot**, it's designed to make Git operations effortless while maintaining full control.

##  About ZaiCode Labs

**ZaiCode Labs** is a team of young, driven innovators using technology to create meaningful impact. We craft custom software solutions that help SMEs and organisations thrive, while building in-house systems that solve real problems we face every day.

**Our Mission:** Technology should make life better — for businesses, communities, and people everywhere.

**What We Do:**
-  Custom software for SMEs
-  Innovative in-house systems
-  Technology for social change
-  Solutions that create meaningful impact

---

##  Key Features

###  AI-Powered Intelligence
- **Smart Commit Messages** - AI analyzes changes and generates conventional commits
- **Conflict Explanation** - Understand merge conflicts in plain English
- **Intelligent Suggestions** - AI-powered recommendations throughout your workflow

###  Smart Automation
- **One-Command Push** - Auto-pull, commit, and push with confirmation
- **Auto-Conflict Resolution** - Interactive step-by-step conflict resolution
- **Smart Pull** - Auto-stash, pull, and re-apply changes
- **Branch Detection** - Automatically works with your current branch

###  **Push Confirmation with Preview**
Before pushing, see exactly what will be pushed:
-  List of commits
-  File changes (added, modified, removed) with full paths
-  Explicit confirmation required

### Beautiful Terminal UI
- Rich colors (#3d4a4b paired with #00eafa)
- Interactive menus
- Progress indicators
- Professional status displays

###  Advanced Features
-  Error rollback on failed operations
-  Progress bars for long operations
-  Undo history (git reflog integration)
-  Interactive commit templates
-  Conflict prevention warnings
-  Performance metrics
-  Team collaboration features

---

##  Requirements

**Mandatory:**
-  **Java 21+** - [Download](https://adoptium.net/)
-  **Git** - Usually pre-installed
-  **Maven 3.6+** - For building from source

**Optional (for AI features):**
-  **Ollama** - [Install Guide](https://ollama.ai/)

---

##  Quick Start

### Method 1: Pre-built Release (Fastest)

```bash
# Download latest release
wget https://github.com/ZaiCodeLabs/zai-git/releases/latest/download/zai-git.jar

# Create installation directory
mkdir -p ~/.zaigit
mv zai-git.jar ~/.zaigit/

# Create executable script
cat > ~/.zaigit/zai-git << 'EOF'
#!/bin/bash
java -jar "$HOME/.zaigit/zai-git.jar" "$@"
EOF

chmod +x ~/.zaigit/zai-git

# Add to PATH
echo 'export PATH="$HOME/.zaigit:$PATH"' >> ~/.bashrc
source ~/.bashrc

# Verify installation
zai-git --version
```

### Method 2: Build from Source

```bash
# Clone repository
git clone https://github.com/ZaiCodeLabs/zai-git.git
cd zai-git

# Make install script executable
chmod +x install.sh

# Build and install
./install.sh --build

# Restart terminal or reload shell
source ~/.bashrc

# Verify installation
zai-git --version
```

---

##  Usage

### Interactive Mode (Recommended)

```bash
# Navigate to your git repository
cd your-project

# Launch ZaiGit
zai-git
```

**Main Menu:**
```
╔════════════ MAIN MENU ════════════╗
  1. 🚀 Smart Push
  2. 📥 Smart Pull
  3. 📊 Status
  4. 🌿 Branch Operations
  5. 📜 Commit History
  6. 💾 Stash Management
  7. ↩️  Undo Last Commit
  8. 🔄 Sync Repository
  9. ⚙️  Settings
  0. Exit
╚═══════════════════════════════════╝
```

### Command-Line Mode

```bash
# Smart push with AI commit message
zai-git push

# Push with custom message
zai-git push -m "feat: add user authentication"

# Smart pull
zai-git pull

# View status
zai-git status

# Sync repository (pull + push)
zai-git sync

# View commit history
zai-git log

# Stash changes
zai-git stash

# Show help
zai-git help
```

---

##  Push Confirmation Example

```bash
$ zai-git push

 Smart Push
 Pulling from origin/feature/payment...
 Pull completed successfully!

 Generating AI commit message...
Generated: feat(payment): integrate Stripe payment gateway

 Changes to be pushed:

╔════════════ COMMITS TO PUSH ════════════╗
  📌 b4c7d9e feat(payment): integrate Stripe payment gateway
     👤 Your Name
╚═════════════════════════════════════════╝

 File Changes:

   ➕ Added (2 files):
      • src/main/java/za/co/zaicodelabs/payment/StripeService.java
      • src/main/java/za/co/zaicodelabs/payment/PaymentController.java

    Modified (1 file):
      • src/main/resources/application.properties

   Total: 3 change(s)

  Push to origin/feature/payment? (Y/n): y

 Pushing to origin/feature/payment...
 Push completed successfully!
```

---

##  Enable AI Features (Optional)

### 1. Install Ollama

```bash
# macOS
brew install ollama

# Linux
curl https://ollama.ai/install.sh | sh

# Windows
# Download from https://ollama.ai/
```

### 2. Pull AI Model

```bash
# Start Ollama service
ollama serve

# In another terminal, pull the model
ollama pull codellama
```

### 3. Configure in ZaiGit

```bash
zai-git
# Select: 9. Settings
# Select: 1. Configure Ollama
# URL: http://localhost:11434 (default)
# Model: codellama (default)
```

### 4. Test Connection

```bash
zai-git
# Select: 9. Settings
# Select: 2. Test Ollama Connection
# Should show: ✅ Ollama is available
```

---

##  Command Reference

| Command | Description |
|---------|-------------|
| `zai-git` | Interactive mode |
| `zai-git push` | Smart push with confirmation |
| `zai-git push -m "msg"` | Push with custom message |
| `zai-git pull` | Smart pull |
| `zai-git sync` | Sync repository (pull + push) |
| `zai-git status` | Show repository status |
| `zai-git branch` | List branches |
| `zai-git branch <name>` | Create branch |
| `zai-git switch <name>` | Switch branch |
| `zai-git merge <name>` | Merge branch |
| `zai-git log` | Show commit history |
| `zai-git stash` | Stash changes |
| `zai-git stash pop` | Apply stashed changes |
| `zai-git undo` | Undo last commit |
| `zai-git help` | Show help |
| `zai-git --version` | Show version |

---

## ️ Technology Stack

| Component | Technology | Purpose |
|-----------|------------|---------|
| **Framework** | Spring Boot 4.0.3 | Application foundation |
| **Git Library** | JGit 6.7.0 | Git operations in Java |
| **Terminal UI** | JLine 3.25.0 | Interactive interface |
| **AI Engine** | Ollama | Commit message generation |
| **Runtime** | Java 21 | Modern JVM features |
| **Build Tool** | Maven | Dependency management |

---

##  Configuration

Edit `src/main/resources/application.properties`:

```properties
# Git Configuration
git.confirm-before-push=true
git.auto-pull-before-push=true
git.default-branch=main

# Ollama Configuration
ollama.url=http://localhost:11434
ollama.model=codellama

# Features
features.ai-commit-messages=true
features.push-confirmation=true
features.auto-conflict-resolution=true
```

---

##  Troubleshooting

### Java Version Issues

```bash
# Check Java version
java -version
# Should show: openjdk version "21" or higher

# Install Java 21
# Ubuntu/Debian:
sudo apt install openjdk-21-jdk

# macOS:
brew install openjdk@21
```

### Command Not Found

```bash
# Reload shell configuration
source ~/.bashrc  # or ~/.zshrc

# Or manually add to PATH
export PATH="$HOME/.zaigit:$PATH"
```

### Ollama Not Available

```bash
# Check if Ollama is running
curl http://localhost:11434/api/tags

# Start Ollama
ollama serve

# Pull model
ollama pull codellama
```

---

##  Contributing

We welcome contributions! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for details.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'feat: add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

##  License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

##  About ZaiCode Labs

We build custom software for SMEs and develop innovative in-house systems that tackle everyday challenges. We're passionate about using technology to drive social change and create meaningful impact.

**Our Belief:** Technology should make life better — for businesses, communities, and people everywhere.

**Brand Colors:**
- Primary: #3d4a4b
- Accent: #00eafa

**Connect with Us:**
-  Website: [zaicodelabs.co.za](https://zaicodelabs.co.za)
-  Email: info@zaicodelabs.co.za

---

##  Support

-  Documentation: [README.md](README.md) | [INSTALLATION.md](INSTALLATION.md)
-  Report Issues: [GitHub Issues](https://github.com/ZaiCodeLabs/zai-git/issues)
-  Discussions: [GitHub Discussions](https://github.com/ZaiCodeLabs/zai-git/discussions)
-  Website: [zaicodelabs.co.za](https://zaicodelabs.co.za)

---

## ️ Acknowledgments

Built with love by the ZaiCode Labs team using:
- [JGit](https://www.eclipse.org/jgit/) - Git implementation in Java
- [JLine 3](https://github.com/jline/jline3) - Terminal UI library
- [Spring Boot](https://spring.io/projects/spring-boot) - Application framework
- [Ollama](https://ollama.ai/) - Local AI integration

---

**Made with ❤️ by ZaiCode Labs**

**Version**: 1.0.0  
**Java**: 21  
**License**: MIT