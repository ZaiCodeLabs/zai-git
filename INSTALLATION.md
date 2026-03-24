# Installation Guide

Complete installation instructions for ZaiGit on all supported platfor.

## Table of Contents

- [System Requirements](#system-requirements)
- [Quick Install](#quick-install)
- [Detailed Installation](#detailed-installation)
- [Building from Source](#building-from-source)
- [Ollama Setup (Optional)](#ollama-setup-optional)
- [Verification](#verification)
- [Troubleshooting](#troubleshooting)
- [Uninstallation](#uninstallation)

## System Requirements

### Required

- **Java**: Version 21 or higher
- **Git**: Any recent version
- **Operating System**: Linux, macOS, or Windows

### Optional (for AI features)

- **Ollama**: For AI-powered commit messages
- **4GB RAM**: Minimum for Ollama models

## Quick Install

### Linux / macOS

```bash
# Download latest release
wget https://github.com/ZaiCodeLabs/zai-git/releases/latest/download/zai-git.jar

# Run installation script
wget https://github.com/ZaiCodeLabs/zai-git/releases/latest/download/install.sh
chmod +x install.sh
./install.sh

# Restart terminal or reload shell
source ~/.bashrc  # or ~/.zshrc for zsh
```

### Windows

```powershell
# Download zai-git.jar from GitHub releases
# Create directory
New-Item -ItemType Directory -Path "$HOME\.zaigit" -Force

# Move JAR
Move-Item zai-git.jar "$HOME\.zaigit\"

# Create batch file
@"
@echo off
java -jar "%USERPROFILE%\.zaigit\zai-git.jar" %*
"@ | Out-File -FilePath "$HOME\.zaigit\zai-git.bat" -Encoding ASCII

# Add to PATH (requires admin)
[Environment]::SetEnvironmentVariable(
    "Path",
    [Environment]::GetEnvironmentVariable("Path", "User") + ";$HOME\.zaigit",
    "User"
)
```

## Detailed Installation

### Step 1: Install Java 21+

#### Linux (Ubuntu/Debian)

```bash
sudo apt update
sudo apt install openjdk-21-jdk
```

#### macOS

```bash
# Using Homebrew
brew install openjdk@21

# Add to PATH
echo 'export PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH"' >> ~/.zshrc
```

#### Windows

Download and install from [Adoptium](https://adoptium.net/):
- Select: JDK 21 (LTS)
- Choose your installer (msi recommended)
- Follow installation wizard

Verify installation:
```bash
java -version
# Should show: openjdk version "21.x.x"
```

### Step 2: Download ZaiGit

#### Option A: Pre-built JAR (Recommended)

```bash
# Latest release
wget https://github.com/ZaiCodeLabs/zai-git/releases/latest/download/zai-git.jar

# Or specific version
wget https://github.com/ZaiCodeLabs/zai-git/releases/download/v1.0.0/zai-git.jar
```

#### Option B: Build from Source

See [Building from Source](#building-from-source) section below.

### Step 3: Install

#### Automatic Installation (Linux/macOS)

```bash
# Download installer
wget https://github.com/ZaiCodeLabs/zai-git/releases/latest/download/install.sh
chmod +x install.sh

# Install
./install.sh
```

#### Manual Installation (All Platforms)

**Linux/macOS:**

```bash
# Create directory
mkdir -p ~/.zaigit

# Copy JAR
cp zai-git.jar ~/.zaigit/

# Create executable
cat > ~/.zaigit/zai-git << 'EOF'
#!/bin/bash
java -jar "$HOME/.zaigit/zai-git.jar" "$@"
EOF

chmod +x ~/.zaigit/zai-git

# Add to PATH
echo 'export PATH="$HOME/.zaigit:$PATH"' >> ~/.bashrc
source ~/.bashrc
```

**Windows:**

```powershell
# Create directory
New-Item -ItemType Directory -Path "$HOME\.zaigit" -Force

# Copy JAR
Copy-Item zai-git.jar "$HOME\.zaigit\"

# Create batch wrapper
@"
@echo off
java -jar "%USERPROFILE%\.zaigit\zai-git.jar" %*
"@ | Out-File -FilePath "$HOME\.zaigit\zai-git.bat" -Encoding ASCII

# Add to PATH (run as Administrator)
$currentPath = [Environment]::GetEnvironmentVariable("Path", "User")
$newPath = "$currentPath;$HOME\.zaigit"
[Environment]::SetEnvironmentVariable("Path", $newPath, "User")
```

### Step 4: Restart Terminal

Close and reopen your terminal to apply PATH changes.

## Building from Source

### Prerequisites

- Java 21+
- Maven 3.8+
- Git

### Clone and Build

```bash
# Clone repository
git clone https://github.com/ZaiCodeLabs/zai-git.git
cd zai-git

# Build with Maven
mvn clean package -DskipTests

# JAR will be in: target/zai-git.jar
```

### Install After Building

```bash
# Use install script with --build flag
chmod +x install.sh
./install.sh --build
```

Or manually:

```bash
cp target/zai-git.jar ~/.zaigit/
```

## Ollama Setup (Optional)

Ollama enables AI-powered commit message generation.

### Install Ollama

#### Linux

```bash
curl -fsSL https://ollama.com/install.sh | sh
```

#### macOS

```bash
# Using Homebrew
brew install ollama
```

#### Windows

Download from [ollama.com/download](https://ollama.com/download)

### Download Model

```bash
# Start Ollama service
ollama serve

# In another terminal, download model
ollama pull codellama

# Or use smaller/faster models
ollama pull llama2
ollama pull mistral
```

### Configure ZaiGit for Ollama

```bash
# Run ZaiGit
zai-git

# Choose: 9. Settings
# Choose: 1. Configure Ollama
# Enter Ollama URL: http://localhost:11434 (default)
# Enter Model: codellama (or your chosen model)
```

### Verify Ollama Connection

```bash
zai-git

# Choose: 9. Settings
# Choose: 2. Test Ollama Connection
# Should show: ✅ Ollama is available
```

## Verification

### Test Installation

```bash
# Check version
zai-git version
# Output: ZaiGit v1.0.0

# View help
zai-git help

# Test in a git repository
cd /path/to/git/repo
zai-git status
```

### Verify PATH

```bash
# Linux/macOS
which zai-git
# Should show: /home/username/.zaigit/zai-git

# Windows
where zai-git
# Should show: C:\Users\username\.zaigit\zai-git.bat
```

### Check Configuration

```bash
zai-git
# Choose: 9. Settings
# Choose: 3. View Configuration
```

Expected output:
```
📁 Git Repository: /current/directory
🌿 Current Branch: main
🤖 Ollama:
   URL: http://localhost:11434
   Model: codellama
   Status: Connected ✓ (or Not available ✗)
⚙️  Features:
   AI Commit Messages: Enabled ✓
   Push Confirmation: Enabled ✓
   Auto-pull Before Push: Enabled ✓
💾 Config File: /home/username/.zaigit/config.json
☕ Java Version: 21.0.2
```

## Troubleshooting

### "command not found: zai-git"

**Solution:**
```bash
# Reload shell configuration
source ~/.bashrc  # or ~/.zshrc

# Or restart terminal

# Verify PATH
echo $PATH | grep zaigit
```

### "Java not found"

**Solution:**
```bash
# Install Java 21
# See Step 1 in Detailed Installation

# Verify
java -version
```

### "Not in a git repository"

**Solution:**
```bash
# Navigate to a git repository
cd /path/to/git/repo

# Or initialize one
git init
```

### "Ollama is not available"

**Solution:**
```bash
# Check if Ollama is running
curl http://localhost:11434/api/tags

# Start Ollama
ollama serve

# Check configuration
zai-git
# Settings → Configure Ollama
# Verify URL is correct
```

### "Permission denied"

**Linux/macOS:**
```bash
chmod +x ~/.zaigit/zai-git
```

**Windows:** Run as Administrator

### "UnsupportedClassVersionError"

**Cause:** Java version too old

**Solution:**
```bash
# Check Java version
java -version

# Must be 21 or higher
# Update Java (see Step 1)
```

### Configuration File Issues

```bash
# View config location
zai-git
# Settings → View Configuration

# Reset config (delete and restart)
rm ~/.zaigit/config.json
zai-git
# Will recreate with defaults
```

## Uninstallation

### Complete Removal

```bash
# Remove installation
rm -rf ~/.zaigit

# Remove from PATH (edit shell config)
# Remove this line from ~/.bashrc or ~/.zshrc:
# export PATH="$HOME/.zaigit:$PATH"

# Reload shell
source ~/.bashrc
```

### Keep Configuration

```bash
# Remove only the JAR
rm ~/.zaigit/zai-git.jar
rm ~/.zaigit/zai-git

# Configuration remains in ~/.zaigit/config.json
```

## Platform-Specific Notes

### Linux

- Tested on: Ubuntu 22.04, Debian 12, Fedora 39
- Works with both bash and zsh
- Requires `wget` or `curl` for downloads

### macOS

- Tested on: macOS 13 (Ventura), macOS 14 (Sonoma)
- Works with zsh (default shell)
- Homebrew recommended for dependencies

### Windows

- Tested on: Windows 10, Windows 11
- PowerShell 5.1+ required
- May need to allow script execution:
  ```powershell
  Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
  ```

## Next Steps

After installation:

1. **Read the docs**: See [HELP.md](HELP.md) for usage guide
2. **Configure settings**: Run `zai-git` → Settings
3. **Try it out**: Navigate to a Git repo and run `zai-git`
4. **Enable AI**: Install Ollama for AI commit messages

## Support

- **Issues**: https://github.com/ZaiCodeLabs/zai-git/issues
- **Discussions**: https://github.com/ZaiCodeLabs/zai-git/discussions
- **Website**: https://zaicodelabs.co.za
- **Email**: support@zaicodelabs.co.za

---

**Installation successful?** Give us a ⭐ on [GitHub](https://github.com/ZaiCodeLabs/zai-git)!
