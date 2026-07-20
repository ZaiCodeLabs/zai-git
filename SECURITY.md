# Security Policy

## Supported Versions

Security fixes are provided for the latest published version of ZaiGit. Users should upgrade to the most recent release before reporting an issue that may already have been corrected.

## Reporting a Vulnerability

Do not disclose security vulnerabilities through public GitHub issues, discussions, or pull requests.

Send reports to [info@zaicodelabs.co.za](mailto:info@zaicodelabs.co.za). An initial response is normally provided within 48 hours.

Include the following information when available:

* A description of the vulnerability and affected component
* The ZaiGit, Java, and operating system versions
* Reproduction instructions
* A minimal proof of concept
* The potential impact
* Any suggested remediation

Remove credentials, access tokens, private repository content, and other sensitive information that is not necessary for investigation.

## Response Process

The maintainers will:

1. Acknowledge receipt of the report.
2. Validate the issue and assess its impact.
3. Develop and test a remediation.
4. Coordinate a disclosure timeline with the reporter.
5. Publish an update and security guidance when appropriate.

Reporter credit is provided with permission.

## Security Considerations

ZaiGit operates on the repository from which it is launched and performs state changing Git operations, including staging, committing, pulling, pushing, stashing, resetting, and branch deletion.

ZaiGit relies on the Git authentication and remote configuration already present on the system. It does not provide a credential store. Users should use SSH keys or a reputable credential helper and should not embed access tokens in remote URLs.

When commit message generation is enabled, repository diff content is sent to the configured Ollama endpoint. Sensitive looking file paths are excluded, but this filtering cannot guarantee that ordinary source files contain no confidential data. Users are responsible for securing the endpoint and reviewing repository content before enabling the feature. Keeping Ollama bound to a trusted local interface is recommended.

Configuration is stored in `~/.zaigit/config.json`. Access to this file is governed by the operating system, directory permissions, and the user account under which ZaiGit runs.

## User Responsibilities

* Review repository changes before confirming state changing operations.
* Maintain current backups and remote copies of important repositories.
* Protect Git credentials and SSH private keys.
* Keep Java, Git, Ollama, and ZaiGit updated.
* Use ZaiGit only in trusted repositories.
* Verify the configured Ollama endpoint before enabling commit message generation.
* Inspect generated commit messages before accepting them.

## Scope

Reports concerning unauthorized file access, credential exposure, unsafe Git state changes, command execution, dependency vulnerabilities, or unintended disclosure to the configured model endpoint are within scope when they can be reproduced in a supported version.

General Git behavior, insecure user supplied remote URLs, compromised local accounts, and vulnerabilities in separately operated services may fall outside the project scope unless ZaiGit materially contributes to the issue.

## Disclosure

The preferred disclosure period is no more than 90 days from confirmation of a vulnerability. Critical issues may require a shorter timeline. Disclosure timing will be coordinated with the reporter when practical.

## Contact

Security reports: [info@zaicodelabs.co.za](mailto:info@zaicodelabs.co.za)

Website: [zaicodelabs.co.za](https://zaicodelabs.co.za)

Last reviewed: July 2026
