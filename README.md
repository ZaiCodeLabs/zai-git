# ZaiGit

ZaiGit is a Git command line application that provides guided repository workflows and optional commit message generation through Ollama.

The application supports common Git operations while allowing users to review repository changes before committing or pushing them.

## Features

* Guided commit and push workflow
* Optional commit messages generated through Ollama
* Pull and repository synchronization workflows
* Repository status and commit history
* Branch creation, switching, and deletion in interactive mode
* Stash management
* Merge conflict detection and guided resolution
* Configurable pull and push confirmation settings
* Support for Linux, macOS, Windows, and WSL

## Requirements

* Java 21 or later
* Git
* Ollama, if commit message generation is required

## Installation

Download a release archive from the [ZaiGit releases page](https://github.com/ZaiCodeLabs/zai-git/releases), or build the application from source:

```bash
git clone https://github.com/ZaiCodeLabs/zai-git.git
cd zai-git
./mvnw verify
./install.sh --build
```

For manual installation and platform guidance, see [INSTALLATION.md](INSTALLATION.md).

## Basic Usage

Run ZaiGit from within a Git repository:

```bash
cd /path/to/repository
zai-git status
zai-git push
```

Running `zai-git` without arguments opens interactive mode.

## Commands

```text
zai-git                 Open interactive mode
zai-git push            Pull, commit changes, and push
zai-git push -m "text"  Commit with the supplied message and push
zai-git pull            Pull changes from the configured remote
zai-git status          Display repository status
zai-git sync            Synchronize the repository
zai-git log             Display recent commit history
zai-git stash           Stash working tree changes
zai-git stash pop       Apply and remove the latest stash
zai-git undo            Undo the latest commit while retaining its changes
zai-git conflicts       Open the conflict resolution workflow
zai-git help            Display command help
zai-git version         Display the installed version
```

Branch management and application settings are available from interactive mode.

## Ollama Integration

ZaiGit can use Ollama to generate a Conventional Commit message from repository changes. Install and start Ollama, then download the configured model:

```bash
ollama serve
ollama pull codellama
```

The Ollama URL, model, and related features can be changed from the settings menu in interactive mode. ZaiGit remains usable when Ollama is unavailable.

Repository diff content may be sent to the configured Ollama endpoint when this feature is enabled. Files with names that indicate credentials, secrets, environment configuration, or private keys are excluded. Users should still review the configured endpoint and generated message before use.

## Authentication

ZaiGit uses the authentication configured for the repository. Configure Git with an SSH key, a credential helper, or another supported authentication method before using remote operations. ZaiGit does not manage or store Git credentials.

## Building and Testing

```bash
./mvnw verify
```

The executable application is created at `target/zai-git.jar`.

## Documentation

* [Installation guide](INSTALLATION.md)
* [Testing guide](TEST.md)
* [Contributing guide](CONTRIBUTING.md)
* [Security policy](SECURITY.md)
* [Code of conduct](CODE_OF_CONDUCT.md)

## Contributing

Contributions are welcome. Review [CONTRIBUTING.md](CONTRIBUTING.md) before opening an issue or pull request.

## License

ZaiGit is available under the [MIT License](LICENSE).

## Contact

ZaiCode Labs is based in South Africa.

Website: [zaicodelabs.co.za](https://zaicodelabs.co.za)

Email: [info@zaicodelabs.co.za](mailto:info@zaicodelabs.co.za)

Support: [GitHub Issues](https://github.com/ZaiCodeLabs/zai-git/issues)
