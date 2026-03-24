---

## **INSTALLATION.md** (Production-Polished)

````markdown
# ZaiGit Installation Guide

AI-Powered Git CLI | Linux | macOS | Windows | Java 21+

---

## Quick Installation (Approx. 30 seconds)

### Linux/macOS
```bash
wget https://github.com/ZaiCodeLabs/zai-git/releases/latest/download/install.sh
chmod +x install.sh
./install.sh
source ~/.bashrc
zai-git --version  # Verify installation
````

### Windows (PowerShell)

```powershell
# Download zai-git.jar from GitHub Releases
# Run installer or follow manual setup below
```

---

## System Requirements

| Required            | Optional                         |
| ------------------- | -------------------------------- |
| Java 21+            | Ollama (for AI commits)          |
| Git                 | 4GB RAM (recommended for Ollama) |
| Linux/macOS/Windows |                                  |

### Verify Java Installation

```bash
java -version  # Must be version 21 or higher
```

---

## Install Java 21+ (If Required)

**Ubuntu/Debian**

```bash
sudo apt update && sudo apt install openjdk-21-jdk
```

**macOS (Homebrew)**

```bash
brew install openjdk@21
```

**Windows**
Download from: [https://adoptium.net/](https://adoptium.net/)

---

## Installation Methods

### 1. Recommended: Automated Installation

```bash
wget https://github.com/ZaiCodeLabs/zai-git/releases/latest/download/install.sh
chmod +x install.sh
./install.sh  # Installs locally (~/.zaigit)

# Optional: make globally accessible
sudo ln -sf ~/.zaigit/zai-git /usr/local/bin/zai-git
```

---

### 2. Manual Installation

```bash
mkdir -p ~/.zaigit

# Download latest release
curl -L https://github.com/ZaiCodeLabs/zai-git/releases/latest/download/zai-git.jar -o ~/.zaigit/zai-git.jar

# Create launcher script
cat > ~/.zaigit/zai-git << 'EOF'
#!/bin/bash
java -jar ~/.zaigit/zai-git.jar "$@"
EOF

chmod +x ~/.zaigit/zai-git

# Add to PATH
echo 'export PATH="$HOME/.zaigit:$PATH"' >> ~/.bashrc
source ~/.bashrc
```

---

### 3. Build from Source

```bash
git clone https://github.com/ZaiCodeLabs/zai-git.git
cd zai-git
mvn clean package -DskipTests
./install.sh --build
```

---

## Git Authentication (Required – One-Time Per Repository)

ZaiGit uses standard Git authentication (same as `git push`).

```bash
# Recommended: HTTPS
git remote set-url origin https://YOUR_USERNAME:ghp_TOKEN@github.com/ORG/REPO.git
```

**Create Personal Access Token (PAT):**

* Visit: [https://github.com/settings/tokens](https://github.com/settings/tokens)
* Enable **repo** scope

---

## Optional: Ollama (AI Commit Messages)

```bash
curl -fsSL https://ollama.com/install.sh | sh
ollama serve &
ollama pull codellama
```

Configure via:

```bash
zai-git settings
```

---

## Verify Installation

```bash
zai-git --version
zai-git help
cd your/git/repo
zai-git status
```

**Expected binary locations:**

* `~/.zaigit/zai-git`
* `/usr/local/bin/zai-git` (if globally installed)

---

## Troubleshooting

| Issue                         | Solution                                   |
| ----------------------------- | ------------------------------------------ |
| `command not found`           | Run `source ~/.bashrc` or restart terminal |
| `Java 21+ required`           | Install Java 21 or higher                  |
| `remote hung up unexpectedly` | Verify HTTPS authentication                |
| `no CredentialsProvider`      | Ensure token is in remote URL              |
| `Ollama unavailable`          | Install or disable AI features             |

---

## First Use

```bash
cd your/git/repo
zai-git status
zai-git push
```

---

## Uninstall

```bash
rm -rf ~/.zaigit

# Remove PATH entry from ~/.bashrc
source ~/.bashrc
```

---

## Support

GitHub: [https://github.com/ZaiCodeLabs/zai-git](https://github.com/ZaiCodeLabs/zai-git)
Email: [info@zaicodelabs.co.za](mailto:info@zaicodelabs.co.za)
Website: [https://zaicodelabs.co.za](https://zaicodelabs.co.za)

```