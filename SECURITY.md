# Security Policy

## Supported Versions

We release patches for security vulnerabilities for the following versions:

| Version | Supported          |
| ------- | ------------------ |
| 1.0.x   | :white_check_mark: |
| < 1.0   | :x:                |

## Reporting a Vulnerability

The ZaiGit team takes security bugs seriously. We appreciate your efforts to responsibly disclose your findings.

### How to Report

**Please do not report security vulnerabilities through public GitHub issues.**

Instead, please report them via email to: **info@zaicodelabs.co.za**

You should receive a response within 48 hours. If for some reason you do not, please follow up via email to ensure we received your original message.

Please include the following information:

- Type of issue (e.g., buffer overflow, SQL injection, cross-site scripting, etc.)
- Full paths of source file(s) related to the manifestation of the issue
- The location of the affected source code (tag/branch/commit or direct URL)
- Any special configuration required to reproduce the issue
- Step-by-step instructions to reproduce the issue
- Proof-of-concept or exploit code (if possible)
- Impact of the issue, including how an attacker might exploit it

### What to Expect

- **Acknowledgment**: We'll acknowledge receipt of your vulnerability report within 48 hours
- **Assessment**: We'll assess the vulnerability and determine its impact and severity
- **Fix Development**: We'll work on a fix and keep you updated on progress
- **Disclosure**: Once fixed, we'll coordinate disclosure with you
- **Credit**: You'll be credited in our security advisories (unless you prefer to remain anonymous)

## Security Considerations

### Local AI Processing

ZaiGit uses Ollama for local AI processing. Key security points:

1. **No Data Leaves Your Machine**: All AI processing happens locally
2. **No API Keys Required**: No cloud service credentials needed
3. **Git Credentials**: ZaiGit uses your system's Git credentials (SSH keys, credential helpers)

### Configuration File

ZaiGit stores configuration in `~/.zaigit/config.json`:

- Contains Ollama URL and model preferences
- Does **NOT** store Git credentials
- Does **NOT** store API keys or tokens
- File permissions: Readable/writable by owner only (600)

### Git Operations

- Uses JGit library for all Git operations
- Respects system Git configuration and credentials
- Does not store or transmit passwords
- SSH keys remain in your `~/.ssh` directory

### Dependencies

We regularly update dependencies to patch known vulnerabilities:

- Spring Boot framework
- JGit for Git operations
- Jackson for JSON processing

Check `pom.xml` for current dependency versions.

## Best Practices for Users

1. **Keep Software Updated**: Always use the latest version of ZaiGit
2. **Secure Your System**: Ensure your SSH keys and Git credentials are properly secured
3. **Review Changes**: Always review the push preview before confirming
4. **Ollama Security**: If running Ollama, ensure it's bound to localhost only
5. **Repository Access**: Only use ZaiGit in repositories you trust

## Security Features

### Built-in Protections

1. **Confirmation Prompts**: Push operations require user confirmation (configurable)
2. **Preview Before Push**: Shows exactly what will be pushed
3. **Local Processing**: No data sent to external services
4. **Read-Only Operations**: Pull and status operations are safe
5. **Conflict Detection**: Warns about conflicts before operations

### What ZaiGit Does NOT Do

- ❌ Store your passwords or tokens
- ❌ Send code to external APIs (unless you configure external Ollama)
- ❌ Modify files without explicit user action
- ❌ Execute arbitrary code from Git repositories
- ❌ Access files outside the current repository

## Vulnerability Disclosure Policy

When we receive a security bug report, we will:

1. Confirm the problem and determine affected versions
2. Audit code to find similar problems
3. Prepare fixes for all supported versions
4. Release patches as soon as possible

### Public Disclosure

- We aim to disclose vulnerabilities within 90 days of initial report
- We'll coordinate disclosure timing with the reporter
- Security advisories will be published on GitHub
- Critical vulnerabilities may be disclosed sooner

## Known Limitations

### Not Security Issues

The following are known limitations and NOT security vulnerabilities:

1. **Local File Access**: ZaiGit operates on local files—this is by design
2. **Git Credentials**: Uses system Git credentials—this is standard Git behavior
3. **Ollama Access**: If Ollama is exposed on your network, secure it separately
4. **Commit Messages**: AI-generated messages are reviewed before commit

## Security Updates

Subscribe to security updates:

- **GitHub**: Watch this repository for security advisories
- **Email**: Subscribe to our mailing list at security@zaicodelabs.co.za
- **RSS**: Follow our GitHub releases feed

## Third-Party Security

### JGit (Eclipse Foundation)

- CVE tracking: https://cve.mitre.org/cgi-bin/cvekey.cgi?keyword=jgit
- Security advisories: https://www.eclipse.org/security/

### Spring Boot (Pivotal)

- CVE tracking: https://spring.io/security
- Security advisories: https://spring.io/blog/category/security-advisories

### Ollama

- If using Ollama, review their security guidelines
- Ensure Ollama is not exposed to untrusted networks
- Keep Ollama updated to latest version

## Compliance

ZaiGit is designed with the following principles:

- **GDPR**: No personal data is collected or transmitted
- **Privacy**: All processing happens locally
- **Open Source**: Code is publicly auditable
- **Transparency**: No hidden data collection or telemetry

## Questions?

If you have questions about security that aren't covered here:

- Email: security@zaicodelabs.co.za
- General questions: Open a GitHub Discussion
- Website: https://zaicodelabs.co.za

---

**Last Updated**: March 2026  
**Version**: 1.0
