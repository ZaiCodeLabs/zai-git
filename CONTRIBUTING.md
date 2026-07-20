# Contributing to ZaiGit

Thank you for contributing to ZaiGit. This guide explains the expected development and review process.

## Before Contributing

Search the [issue tracker](https://github.com/ZaiCodeLabs/zai-git/issues) before opening a new issue. For significant changes, open an issue first so that the proposed behavior and implementation can be discussed.

All participation must follow the [Code of Conduct](CODE_OF_CONDUCT.md).

## Development Requirements

* Java 21 or later
* Git
* The included Maven wrapper

## Development Setup

```bash
git clone https://github.com/YOUR_USERNAME/zai-git.git
cd zai-git
./mvnw verify
```

Create a focused branch for the change:

```bash
git checkout -b feat/short-description
```

## Coding Standards

* Use four spaces for indentation.
* Prefer clear names and small, focused methods.
* Keep business logic separate from terminal input and output where practical.
* Handle expected failures with clear user messages and meaningful process status codes.
* Add tests for new behavior and regression fixes.
* Avoid unrelated formatting or dependency changes in the same pull request.

## Commit Messages

Use the [Conventional Commits](https://www.conventionalcommits.org/) format:

```text
feat(cli): add branch deletion
fix(ollama): handle unavailable model
docs: clarify installation requirements
test(git): cover stash application
```

## Testing

Run the complete verification lifecycle before submitting a pull request:

```bash
./mvnw verify
```

Manual testing should use a disposable Git repository when the change affects commits, branches, resets, stashes, conflicts, or remote operations.

## Pull Requests

A pull request should include:

* A concise description of the change
* The reason the change is needed
* Relevant implementation or compatibility notes
* Automated and manual testing performed
* Documentation updates when behavior changes
* A reference to the related issue, when applicable

Keep each pull request limited to one logical change. Review feedback should be addressed with additional commits or clearly explained responses.

## Reporting Defects

Include the following information in a defect report:

* ZaiGit version
* Operating system and Java version
* Repository state relevant to the problem
* Exact steps required to reproduce the issue
* Expected and actual behavior
* Error output with secrets and credentials removed

Security vulnerabilities must be reported according to [SECURITY.md](SECURITY.md), not through a public issue.

## Documentation Changes

Documentation should be concise, accurate, and written for readers who may not be familiar with the implementation. Preserve command names, filenames, flags, URLs, and code identifiers exactly.

## Contact

Questions may be raised through [GitHub Discussions](https://github.com/ZaiCodeLabs/zai-git/discussions) or sent to [info@zaicodelabs.co.za](mailto:info@zaicodelabs.co.za).
