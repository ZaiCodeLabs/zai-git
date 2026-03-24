#!/bin/bash

# ZaiGit Installation Script 🚀
# ZaiCode Labs | https://zaicodelabs.co.za
# Usage: ./install.sh [--build]

set -e

INSTALL_DIR="$HOME/.zaigit"
BIN_NAME="zai-git"

echo "╔═══════════════════════════════════════════════╗"
echo "║     ZaiGit v1.0.0 - AI Git CLI Installation   ║"
echo "║              ZaiCode Labs                     ║"
echo "╚═══════════════════════════════════════════════╝"
echo ""

# Check Java version
if ! command -v java &> /dev/null; then
    echo "❌ Java not found. Install Java 21+:"
    echo "  Ubuntu: sudo apt install openjdk-21-jdk"
    echo "  macOS: brew install openjdk@21"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 21 ]; then
    echo "❌ Java 21+ required (found $JAVA_VERSION)"
    echo "  Upgrade: sudo apt update && sudo apt install openjdk-21-jdk"
    exit 1
fi

echo "Java $JAVA_VERSION ✓"

# Build if --build flag provided
if [ "$1" == "--build" ]; then
    echo "Building from source..."

    if ! command -v mvn &> /dev/null; then
        echo "Maven required for build. Install: sudo apt install maven"
        exit 1
    fi

    mvn clean package -DskipTests
    JAR_FILE="target/zai-git.jar"
    echo "Built successfully"
else
    JAR_FILE="zai-git.jar"

    if [ ! -f "$JAR_FILE" ]; then
        echo "zai-git.jar not found."
        echo "Run: ./install.sh --build  (builds from source)"
        echo "Or: Download pre-built JAR from GitHub Releases"
        exit 1
    fi
fi

# Create installation directory
mkdir -p "$INSTALL_DIR"
echo "Install dir: $INSTALL_DIR"

# Copy JAR
cp "$JAR_FILE" "$INSTALL_DIR/"
echo "JAR installed"

# Create executable wrapper
cat > "$INSTALL_DIR/$BIN_NAME" << 'EOF'
#!/bin/bash
# ZaiGit Wrapper - ZaiCode Labs
cd "$(dirname "$0")"
java -jar "zai-git.jar" "$@"
EOF

chmod +x "$INSTALL_DIR/$BIN_NAME"
echo "Executable created: $INSTALL_DIR/$BIN_NAME"

# Add to PATH (user-local first, then global option)
if ! grep -q "$INSTALL_DIR" "$HOME/.bashrc" 2>/dev/null; then
    echo "" >> "$HOME/.bashrc"
    echo '# ZaiGit - AI Git CLI by ZaiCode Labs' >> "$HOME/.bashrc"
    echo 'export PATH="$HOME/.zaigit:$PATH"' >> "$HOME/.bashrc"
    echo "Added to ~/.bashrc"
    echo "Restart terminal or: source ~/.bashrc"
fi

# Global install option
echo ""
read -p "Install globally? (sudo /usr/local/bin) [y/N]: " GLOBAL
if [[ $GLOBAL =~ ^[Yy]$ ]]; then
    sudo ln -sf "$INSTALL_DIR/$BIN_NAME" /usr/local/bin/$BIN_NAME
    echo "🌍 Global install complete: /usr/local/bin/zai-git"
fi

echo ""
echo "╔═══════════════════════════════════════════════╗"
echo "║             Installation SUCCESSFUL!          ║"
echo "╚═══════════════════════════════════════════════╝"
echo ""
echo "  Test ZaiGit:"
echo "  cd /path/to/git/repo"
echo "  zai-git status"
echo ""
echo "  CRITICAL: Setup auth ONCE per repo:"
echo "  git remote set-url origin https://USER:ghp_TOKEN@github.com/ORG/REPO.git"
echo ""
echo "  Full guide: zai-git help"
echo "🌐 Website: https://zaicodelabs.co.za"
echo "⭐ GitHub: github.com/ZaiCodeLabs/zai-git"
