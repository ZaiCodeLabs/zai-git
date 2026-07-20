# ZaiGit Installation Guide

This guide describes how to install ZaiGit from a release or from source.

## System Requirements

* Java 21 or later
* Git
* A supported terminal on Linux, macOS, Windows, or WSL
* Ollama, if commit message generation is required

Verify the installed Java version:

```bash
java -version
```

## Install from a Release

Download `zai-git.jar` and `install.sh` from the [latest release](https://github.com/ZaiCodeLabs/zai-git/releases/latest), place them in the same directory, and run:

```bash
chmod +x install.sh
./install.sh
```

The installer places the application in `~/.zaigit` and can create a link in `/usr/local/bin` when requested.

Restart the terminal after installation, or reload the relevant shell configuration. The current installer updates `~/.bashrc`:

```bash
source ~/.bashrc
zai-git version
```

## Build and Install from Source

```bash
git clone https://github.com/ZaiCodeLabs/zai-git.git
cd zai-git
./mvnw verify
./install.sh --build
```

## Manual Installation

Create an installation directory and copy the packaged application:

```bash
mkdir -p ~/.zaigit
cp target/zai-git.jar ~/.zaigit/zai-git.jar
```

Create a launcher named `~/.zaigit/zai-git` with the following content:

```bash
#!/usr/bin/env bash
exec java -jar "$HOME/.zaigit/zai-git.jar" "$@"
```

Make the launcher executable and add its directory to `PATH`:

```bash
chmod +x ~/.zaigit/zai-git
export PATH="$HOME/.zaigit:$PATH"
```

Add the `PATH` export to the appropriate shell configuration file to retain it between sessions.

## Git Authentication

ZaiGit relies on the authentication already configured for Git. Recommended options include:

* SSH keys
* Git Credential Manager
* A platform credential helper

Verify remote access with standard Git before using ZaiGit:

```bash
git remote -v
git fetch
```

Do not place access tokens directly in repository URLs because they can be exposed through configuration files, logs, and command history.

## Ollama Configuration

Install Ollama according to its platform documentation, then start the service and download a model:

```bash
ollama serve
ollama pull codellama
```

Open `zai-git` in interactive mode and select the settings menu to configure the Ollama endpoint and model.

## Verify the Installation

```bash
zai-git version
zai-git help
cd /path/to/repository
zai-git status
```

## Troubleshooting

### Command not found

Confirm that `~/.zaigit` is included in `PATH`, then restart the terminal or reload the shell configuration.

### Unsupported Java version

Install Java 21 or later and confirm that the correct `java` executable appears first in `PATH`.

### Git authentication failure

Run `git fetch` or `git push` directly to validate the repository credentials and remote configuration.

### Ollama unavailable

Confirm that Ollama is running and that the configured URL and model are correct. Commit message generation can also be disabled from the settings menu.

## Uninstall

Remove the installation directory and any link created in `/usr/local/bin`:

```bash
rm -rf ~/.zaigit
sudo rm /usr/local/bin/zai-git
```

Remove the ZaiGit `PATH` entry from the shell configuration if it was added manually.

## Support

Repository: [github.com/ZaiCodeLabs/zai-git](https://github.com/ZaiCodeLabs/zai-git)

Email: [info@zaicodelabs.co.za](mailto:info@zaicodelabs.co.za)
