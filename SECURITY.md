# Security Policy

---

## Supported Versions

| Version | Supported |
|---------|-----------|
| 1.0.x   | Yes       |
| < 1.0   | No        |

---

## Reporting Vulnerabilities

Please do not report security vulnerabilities through public GitHub issues.

**Contact:** info@zaicodelabs.co.za  
**Response Time:** Within 48 hours

### Please include the following details:
- Type of issue (e.g., buffer overflow, injection, XSS)
- Affected file paths and line numbers
- Step-by-step reproduction instructions
- Proof-of-concept (if available)
- Impact assessment

---

## Our Response Process

1. **Acknowledge** – Within 48 hours
2. **Assess** – Determine severity and impact
3. **Remediate** – Apply fixes to supported versions
4. **Coordinate** – Align disclosure timeline with reporter
5. **Credit** – Acknowledge reporter (optional anonymity)

---

## Security Model

- Local-only processing (no external code transmission)
- Uses existing Git authentication (SSH or Personal Access Tokens)
- Does not store credentials
- Configuration stored at `~/.zaigit/config.json` with restricted permissions
- Explicit user confirmation required before push operations
- Built on actively maintained dependencies (JGit, Spring Boot)

---

## Out of Scope (Non-Goals)

ZaiGit does not:

- Store user credentials
- Transmit source code to external services
- Modify files without user confirmation
- Execute repository code
- Access files outside the working directory
- Collect telemetry or usage data

---

## User Security Best Practices

- Keep ZaiGit updated to the latest version
- Protect Git credentials and SSH keys
- Review all changes before confirming pushes
- Restrict Ollama to localhost if enabled
- Use ZaiGit within trusted repositories

---

## Known Limitations

The following are expected behaviors and not considered vulnerabilities:

1. Git authentication relies on system configuration
2. Local file access is required for Git operations
3. Ollama security depends on user configuration

---

## Dependencies

| Library       | Maintainer   | Security Advisory |
|--------------|--------------|-------------------|
| JGit         | Eclipse      | eclipse.org/security |
| Spring Boot  | VMware       | spring.io/security |
| Jackson      | FasterXML    | cowtowncoder.com |

---

## Disclosure Policy

- Maximum disclosure window: 90 days from initial report
- Critical vulnerabilities may follow an accelerated timeline
- Disclosure is coordinated with the reporting party

---

## Compliance & Privacy

- No telemetry or user tracking
- No external data transmission
- Fully local processing model
- Open-source and publicly auditable

---

## Contact

General: info@zaicodelabs.co.za  
Website: https://zaicodelabs.co.za

---

**Last Updated:** March 2026  
**ZaiCode Labs (Pty) Ltd**  
South Africa