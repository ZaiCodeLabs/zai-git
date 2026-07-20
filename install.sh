#!/usr/bin/env bash

set -Eeuo pipefail

readonly APPLICATION_NAME="ZaiGit"
readonly BINARY_NAME="zai-git"
readonly REQUIRED_JAVA_VERSION=21
readonly INSTALL_DIRECTORY="${HOME}/.zaigit"
readonly INSTALLED_JAR="${INSTALL_DIRECTORY}/zai-git.jar"
readonly INSTALLED_LAUNCHER="${INSTALL_DIRECTORY}/${BINARY_NAME}"
readonly GLOBAL_LAUNCHER="/usr/local/bin/${BINARY_NAME}"

SCRIPT_DIRECTORY="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
BUILD_FROM_SOURCE=false
INSTALL_GLOBALLY=false
UPDATE_PATH=true

print_usage() {
    printf '%s\n' \
        "Usage: ./install.sh [options]" \
        "" \
        "Options:" \
        "  --build       Build and test the application before installation" \
        "  --global      Create a launcher in /usr/local/bin" \
        "  --no-path     Do not update the shell PATH configuration" \
        "  --help        Display this help message"
}

fail() {
    printf 'Error: %s\n' "$1" >&2
    exit 1
}

parse_arguments() {
    while (($# > 0)); do
        case "$1" in
            --build)
                BUILD_FROM_SOURCE=true
                ;;
            --global)
                INSTALL_GLOBALLY=true
                ;;
            --no-path)
                UPDATE_PATH=false
                ;;
            --help|-h)
                print_usage
                exit 0
                ;;
            *)
                fail "Unknown option: $1. Run ./install.sh --help for usage information."
                ;;
        esac
        shift
    done
}

check_java() {
    command -v java >/dev/null 2>&1 || fail \
        "Java ${REQUIRED_JAVA_VERSION} or later is required but was not found in PATH."

    local version_output
    local version_string
    local major_version

    version_output="$(java -version 2>&1)"
    version_string="$(printf '%s\n' "$version_output" | awk -F '"' '/version/ {print $2; exit}')"
    major_version="${version_string%%.*}"

    if [[ "$major_version" == "1" ]]; then
        major_version="$(printf '%s' "$version_string" | cut -d. -f2)"
    fi

    [[ "$major_version" =~ ^[0-9]+$ ]] || fail \
        "Unable to determine the installed Java version."

    ((major_version >= REQUIRED_JAVA_VERSION)) || fail \
        "Java ${REQUIRED_JAVA_VERSION} or later is required. Java ${major_version} is currently active."

    printf 'Java %s detected.\n' "$major_version"
}

build_application() {
    local wrapper="${SCRIPT_DIRECTORY}/mvnw"

    [[ -f "$wrapper" ]] || fail "The Maven wrapper was not found at ${wrapper}."
    [[ -x "$wrapper" ]] || chmod +x "$wrapper"

    printf 'Building and testing %s.\n' "$APPLICATION_NAME"
    (
        cd "$SCRIPT_DIRECTORY"
        ./mvnw verify
    )
}

resolve_jar() {
    local jar_path

    if [[ "$BUILD_FROM_SOURCE" == true ]]; then
        build_application
        jar_path="${SCRIPT_DIRECTORY}/target/zai-git.jar"
    elif [[ -f "${SCRIPT_DIRECTORY}/zai-git.jar" ]]; then
        jar_path="${SCRIPT_DIRECTORY}/zai-git.jar"
    elif [[ -f "${SCRIPT_DIRECTORY}/target/zai-git.jar" ]]; then
        jar_path="${SCRIPT_DIRECTORY}/target/zai-git.jar"
    else
        fail "No packaged application was found. Place zai-git.jar beside this installer or run ./install.sh --build."
    fi

    [[ -r "$jar_path" ]] || fail "The application archive is not readable: ${jar_path}"
    printf '%s' "$jar_path"
}

install_application() {
    local source_jar="$1"

    install -d -m 0755 "$INSTALL_DIRECTORY"
    install -m 0644 "$source_jar" "$INSTALLED_JAR"

    create_launcher
}

create_launcher() {
    cat > "$INSTALLED_LAUNCHER" <<'EOF'
#!/usr/bin/env bash

set -e
exec java -jar "${HOME}/.zaigit/zai-git.jar" "$@"
EOF
    chmod 0755 "$INSTALLED_LAUNCHER"

    printf 'Installed application: %s\n' "$INSTALLED_JAR"
    printf 'Installed launcher: %s\n' "$INSTALLED_LAUNCHER"
}

shell_configuration_file() {
    case "${SHELL:-}" in
        */zsh)
            printf '%s' "${HOME}/.zshrc"
            ;;
        */bash)
            printf '%s' "${HOME}/.bashrc"
            ;;
        *)
            printf '%s' "${HOME}/.profile"
            ;;
    esac
}

update_path_configuration() {
    if [[ "$UPDATE_PATH" != true ]]; then
        return 0
    fi

    local config_file
    local path_entry='export PATH="$HOME/.zaigit:$PATH"'
    config_file="$(shell_configuration_file)"

    if [[ -f "$config_file" ]] && grep -Fqx "$path_entry" "$config_file"; then
        printf 'PATH is already configured in %s.\n' "$config_file"
        return
    fi

    printf '\n%s\n' "$path_entry" >> "$config_file"
    printf 'Added ZaiGit to PATH in %s.\n' "$config_file"
    printf 'Restart the terminal or reload that file before using %s.\n' "$BINARY_NAME"
}

install_global_launcher() {
    if [[ "$INSTALL_GLOBALLY" != true ]]; then
        return 0
    fi

    command -v sudo >/dev/null 2>&1 || fail \
        "The --global option requires sudo."

    sudo ln -sfn "$INSTALLED_LAUNCHER" "$GLOBAL_LAUNCHER"
    printf 'Created global launcher: %s\n' "$GLOBAL_LAUNCHER"
}

print_completion_message() {
    printf '\n%s installation completed successfully.\n' "$APPLICATION_NAME"
    printf '%s\n' \
        "" \
        "Run ZaiGit from within a Git repository:" \
        "" \
        "  cd /path/to/repository" \
        "  zai-git status" \
        "" \
        "ZaiGit uses the repository's existing Git authentication. Configure" \
        "an SSH key or credential helper before performing remote operations." \
        "" \
        "Documentation: https://github.com/ZaiCodeLabs/zai-git"
}

main() {
    parse_arguments "$@"
    check_java

    local source_jar
    source_jar="$(resolve_jar)"

    install_application "$source_jar"
    update_path_configuration
    install_global_launcher
    print_completion_message
}

main "$@"
