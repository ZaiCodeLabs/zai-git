## **CONTRIBUTING.md** (Startup-Optimized)

```markdown
# Contributing to ZaiGit

Thank you for your interest in ZaiGit! 

## Quick Start

1. **Fork** → **Clone** → **Build**:
```bash
git clone https://github.com/YOUR_USERNAME/zai-git.git
cd zai-git
mvn clean install
java -jar target/zai-git.jar
```

2. **Create feature branch**:
```bash
git checkout -b feat/your-feature
```

3. **Commit** → **Push** → **PR**

## What to Contribute

-  **Bug reports** (detailed repro steps)
-  **Features** (check [issues](https://github.com/ZaiCodeLabs/zai-git/issues))
-  **Docs** (README, INSTALLATION.md)
-  **Bug fixes** (`good first issue` label)

## Development

### Prerequisites
- Java 21+
- Maven 3.8+
- Git

### Run Tests
```bash
mvn test
```

### Coding Standards
- 4-space indents
- 120 char lines
- [Conventional Commits](https://www.conventionalcommits.org/)
```
feat(cli): add branch delete
fix(ollama): fix streaming response
docs: update installation
```

## Pull Requests

** Great PRs include:**
```
Title: feat(cli): add your feature name

Description:
- What it does
- Why it's useful
- Tests added

Closes #123
```

**PR Checklist:**
- [ ] Tests pass (`mvn test`)
- [ ] Docs updated (if needed)
- [ ] Conventional commit messages

## Issues

**Bug reports need:**
```
Environment: Ubuntu 22.04, Java 21, ZaiGit v1.0.0
Steps to reproduce:
1. ...
Expected: ...
Actual: ...
```

## Get Help

- **Issues** → Bugs/features
- **Discussions** → Questions/ideas
- **Email**: info@zaicodelabs.co.za

## Thanks!

All contributors get:
- GitHub credit
- Release mentions
- ZaiCode Labs swag (future)

---

**Made with ❤️ by ZaiCode Labs** | [zaicodelabs.co.za](https://zaicodelabs.co.za)
```