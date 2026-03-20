#!/bin/bash

# ZaiGit Installation Script
# Usage: ./install.sh [--build]

set -e

INSTALL_DIR="$HOME/.zaigit"
BIN_NAME="zai-git"

echo "╔════════════════════════════════════════╗"
echo "║   ZaiGit Installation                  ║"
echo "║   Built with ❤️  by ZaiCode Labs      ║"
echo "╚════════════════════════════════════════╝"
echo ""

# Check Java version
if ! command -v java &> /dev/null; then
    echo "❌ Java not found. Please install Java 21+"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 21 ]; then
    echo "❌ Java 21+ required. Found Java $JAVA_VERSION"
    exit 1
fi

echo "✓ Java $JAVA_VERSION detected"

# Build if --build flag provided
if [ "$1" == "--build" ]; then
    echo "Building from source..."
    
    if ! command -v mvn &> /dev/null; then
        echo "❌ Maven not found. Please install Maven"
        exit 1
    fi
    
    mvn clean package -DskipTests
    JAR_FILE="target/zai-git.jar"
else
    JAR_FILE="zai-git.jar"
    
    if [ ! -f "$JAR_FILE" ]; then
        echo "❌ zai-git.jar not found. Run with --build to build from source"
        exit 1
    fi
fi

# Create installation directory
echo "Creating installation directory..."
mkdir -p "$INSTALL_DIR"

# Copy JAR
echo "Installing ZaiGit..."
cp "$JAR_FILE" "$INSTALL_DIR/"

# Create executable script
cat > "$INSTALL_DIR/$BIN_NAME" << 'EOF'
#!/bin/bash
java -jar "$HOME/.zaigit/zai-git.jar" "$@"
EOF

chmod +x "$INSTALL_DIR/$BIN_NAME"

# Add to PATH
SHELL_CONFIG=""
if [ -n "$BASH_VERSION" ]; then
    SHELL_CONFIG="$HOME/.bashrc"
elif [ -n "$ZSH_VERSION" ]; then
    SHELL_CONFIG="$HOME/.zshrc"
fi

if [ -n "$SHELL_CONFIG" ]; then
    if ! grep -q ".zaigit" "$SHELL_CONFIG"; then
        echo "" >> "$SHELL_CONFIG"
        echo '# ZaiGit' >> "$SHELL_CONFIG"
        echo 'export PATH="$HOME/.zaigit:$PATH"' >> "$SHELL_CONFIG"
        echo "✓ Added to PATH in $SHELL_CONFIG"
    fi
fi

echo ""
echo "╔════════════════════════════════════════╗"
echo "║   ✓ Installation Complete!             ║"
echo "╚════════════════════════════════════════╝"
echo ""
echo "To use ZaiGit:"
echo "  1. Restart your terminal or run: source $SHELL_CONFIG"
echo "  2. Navigate to a git repository"
echo "  3. Run: zai-git"
echo ""
echo "For help: zai-git help"
echo "Website: https://zaicodelabs.co.za"
