# Contributing to ZaiGit

First off, thank you for considering contributing to ZaiGit! It's people like you that make ZaiGit such a great tool.

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [How Can I Contribute?](#how-can-i-contribute)
- [Development Setup](#development-setup)
- [Coding Standards](#coding-standards)
- [Commit Messages](#commit-messages)
- [Pull Request Process](#pull-request-process)
- [Testing](#testing)
- [Documentation](#documentation)

## Code of Conduct

This project and everyone participating in it is governed by our [Code of Conduct](CODE_OF_CONDUCT.md). By participating, you are expected to uphold this code. Please report unacceptable behavior to conduct@zaicodelabs.co.za.

## How Can I Contribute?

###  Reporting Bugs

Before creating bug reports, please check the existing issues to avoid duplicates.

**When reporting bugs, include:**

- **Clear title**: Summarize the problem in one sentence
- **Detailed description**: Explain what happened vs. what you expected
- **Steps to reproduce**: List exact steps to trigger the bug
- **Environment**:
  - OS (Ubuntu 22.04, macOS 14, Windows 11, etc.)
  - Java version (`java -version`)
  - ZaiGit version (`zai-git version`)
  - Git version (`git --version`)
  - Ollama version if using AI (`ollama --version`)
- **Logs/Output**: Include relevant error messages or screenshots
- **Configuration**: Include `~/.zaigit/config.json` (remove sensitive data)

**Example Bug Report:**

```markdown
**Title:** Smart Push fails with NullPointerException

**Description:**
When running `zai-git push`, the application crashes with a NullPointerException
instead of showing the push preview.

**Steps to Reproduce:**
1. Make changes to a file
2. Run `zai-git push`
3. Observe error

**Environment:**
- OS: Ubuntu 22.04
- Java: OpenJDK 21.0.2
- ZaiGit: v1.0.0
- Git: 2.34.1

**Error Output:**
```
java.lang.NullPointerException: Cannot invoke "String.isEmpty()" because "message" is null
    at za.co.zaicodelabs.zaigit.cli.GitCLIController.smartPush
```

**Expected:** Should show push preview
**Actual:** Crashes with NullPointerException
```

###  Suggesting Features

We love new ideas! Before suggesting a feature:

1. Check if it's already been suggested
2. Consider if it aligns with ZaiGit's goals
3. Think about how it would benefit users

**Feature Request Template:**

```markdown
**Title:** Add ability to configure commit message templates

**Problem:**
Users want consistent commit message format across their team.

**Proposed Solution:**
Add a setting for commit message templates that AI should follow.

**Alternatives Considered:**
- Manual editing (too time-consuming)
- Pre-commit hooks (outside ZaiGit's scope)

**Additional Context:**
Could integrate with conventional commits standards.

**Would you like to implement this?** Yes/No
```

###  Improving Documentation

Documentation improvements are always welcome!

- Fix typos or unclear explanations
- Add examples or use cases
- Improve installation instructions
- Translate documentation

No need for issues—just submit a PR!

###  Code Contributions

Pick an issue labeled:
- `good first issue` - Perfect for newcomers
- `help wanted` - Need community help
- `enhancement` - New features
- `bug` - Bug fixes

## Development Setup

### Prerequisites

- **Java 21+**
- **Maven 3.8+**
- **Git**
- **IDE**: IntelliJ IDEA (recommended) or Eclipse
- **Ollama** (optional, for testing AI features)

### 1. Fork and Clone

```bash
# Fork on GitHub first, then:
git clone https://github.com/YOUR_USERNAME/zai-git.git
cd zai-git
```

### 2. Build

```bash
# Clean build
mvn clean install

# Skip tests for faster build
mvn clean install -DskipTests

# Run application
java -jar target/zai-git.jar
```

### 3. IDE Setup

**IntelliJ IDEA:**

1. File → Open → Select `pom.xml`
2. Import as Maven project
3. Set SDK to Java 21+
4. Build → Build Project

**Eclipse:**

1. File → Import → Existing Maven Projects
2. Select project directory
3. Configure Java 21+ in Build Path

### 4. Run from IDE

**Main class:** `za.co.zaicodelabs.zaigit.ZaiGitApplication`

**Program arguments (optional):**
- Interactive mode: (none)
- Command mode: `push`, `pull`, `status`, etc.

### 5. Set Up Ollama (Optional)

```bash
# Install Ollama
curl -fsSL https://ollama.com/install.sh | sh

# Start service
ollama serve

# Download model
ollama pull codellama
```

## Coding Standards

### Java Style Guide

We follow standard Java conventions:

- **Indentation**: 4 spaces (no tabs)
- **Line length**: 120 characters max
- **Braces**: Egyptian style (opening brace on same line)
- **Naming**:
  - Classes: `PascalCase`
  - Methods/Variables: `camelCase`
  - Constants: `UPPER_SNAKE_CASE`
  - Packages: `lowercase`

### Code Organization

```
src/main/java/za/co/zaicodelabs/zaigit/
├── ZaiGitApplication.java       # Main entry point
├── cli/
│   └── GitCLIController.java   # CLI interface
└── service/
    ├── GitService.java          # Git operations
    ├── OllamaService.java       # AI integration
    ├── UIService.java           # User interface
    ├── ConfigService.java       # Configuration
    └── ConflictResolverService.java  # Conflict resolution
```

### Best Practices

**DO:**
-  Write self-documenting code
-  Add JavaDoc for public methods
-  Handle exceptions appropriately
-  Use dependency injection
-  Keep methods focused and small
-  Write tests for new features

**DON'T:**
-  Leave commented-out code
-  Use `System.out.println()` (use UIService)
-  Hardcode values (use ConfigService)
-  Catch exceptions without handling
-  Create God classes

### Example Code

**Good:**

```java
public void smartPush() {
    try {
        uiService.progress("Pulling from remote...");
        gitService.pull();
        uiService.clearProgress();
        uiService.success("✅ Pull completed");
        
        if (!gitService.hasChanges()) {
            uiService.warning("No changes to commit");
            return;
        }
        
        // ... rest of logic
    } catch (GitAPIException e) {
        uiService.clearProgress();
        uiService.error("Push failed: " + e.getMessage());
    }
}
```

**Bad:**

```java
public void smartPush() {
    System.out.println("Pulling...");
    git.pull(); // No error handling!
    if (status.isEmpty()) { // What status?
        System.out.println("No changes");
    }
    // ... rest of logic
}
```

## Commit Messages

We follow [Conventional Commits](https://www.conventionalcommits.org/):

### Format

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Types

- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation only
- `style`: Code style (formatting, no logic change)
- `refactor`: Code change (no bug fix or feature)
- `test`: Adding or updating tests
- `chore`: Build process, dependencies, etc.
- `perf`: Performance improvement
- `ci`: CI/CD changes

### Examples

```bash
feat(cli): add branch delete command

Implement branch deletion with force option in branch operations menu.
Includes validation and confirmation prompt.

Closes #42

---

fix(ollama): handle streaming response correctly

Parse each line of Ollama response separately instead of only first chunk.

Fixes #38

---

docs: update installation instructions for Windows

Add PowerShell commands and note about execution policy.

---

chore(deps): bump spring-boot to 3.2.3
```

### Rules

- Use imperative mood ("add" not "added")
- Don't capitalize first letter
- No period at the end
- Keep subject line under 50 characters
- Wrap body at 72 characters
- Reference issues/PRs when relevant

## Pull Request Process

### 1. Create Feature Branch

```bash
# From main branch
git checkout main
git pull origin main

# Create feature branch
git checkout -b feat/your-feature-name
# or
git checkout -b fix/your-bug-fix
```

### 2. Make Changes

```bash
# Make your changes
# Commit with conventional commits
git add .
git commit -m "feat(scope): description"

# Keep commits focused - one logical change per commit
```

### 3. Update Documentation

If your changes affect:
- User-facing features → Update HELP.md
- Installation → Update INSTALLATION.md
- API/Code → Update JavaDoc
- Configuration → Update HELP.md configuration section

### 4. Run Tests

```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=GitServiceTest

# Check for compilation errors
mvn clean compile
```

### 5. Push and Create PR

```bash
# Push to your fork
git push origin feat/your-feature-name

# Go to GitHub and create Pull Request
```

### 6. PR Description Template

```markdown
## Description
Brief description of what this PR does.

## Type of Change
- [ ] Bug fix (non-breaking change fixing an issue)
- [ ] New feature (non-breaking change adding functionality)
- [ ] Breaking change (fix or feature causing existing functionality to break)
- [ ] Documentation update

## Related Issue
Fixes #(issue number)

## Testing
Describe how you tested:
- [ ] Unit tests added/updated
- [ ] Manual testing performed
- [ ] Tested with Ollama enabled
- [ ] Tested with Ollama disabled

## Checklist
- [ ] Code follows style guidelines
- [ ] Self-review performed
- [ ] Comments added for complex code
- [ ] Documentation updated
- [ ] No new warnings generated
- [ ] Tests added/updated
- [ ] All tests pass
- [ ] Conventional commit messages used

## Screenshots (if applicable)
Add screenshots for UI changes.

## Additional Notes
Any other context about the PR.
```

### 7. Code Review

- Respond to feedback promptly
- Make requested changes
- Push updates to same branch
- Mark conversations as resolved

### 8. Merge

Once approved:
- Maintainer will merge
- Delete your feature branch
- Pull latest main for next contribution

## Testing

### Unit Tests

```java
@Test
void testSmartPush() {
    // Arrange
    when(gitService.hasChanges()).thenReturn(true);
    when(ollamaService.generateCommitMessage(any()))
        .thenReturn("feat: test commit");
    
    // Act
    controller.smartPush();
    
    // Assert
    verify(gitService).commitAndPush("feat: test commit");
}
```

### Integration Tests

Test full workflows:
- Smart push with AI
- Conflict resolution
- Configuration changes

### Manual Testing Checklist

- [ ] Interactive mode works
- [ ] Command mode works
- [ ] All menu options function
- [ ] AI commit messages generate (with Ollama)
- [ ] Manual commit messages work (without Ollama)
- [ ] Push confirmation works
- [ ] Conflict resolution works
- [ ] Settings save and load
- [ ] Error handling is graceful

## Documentation

### JavaDoc

**Public methods require JavaDoc:**

```java
/**
 * Performs a smart push operation with auto-pull and AI commit message.
 * 
 * @throws GitAPIException if Git operation fails
 * @throws IOException if file I/O fails
 */
public void smartPush() throws GitAPIException, IOException {
    // ...
}
```

### README Updates

Update README.md if you:
- Add new features
- Change usage
- Modify requirements

### Changelog

Add entry to CHANGELOG.md:

```markdown
## [Unreleased]

### Added
- Branch delete functionality with force option

### Fixed
- Ollama streaming response parsing

### Changed
- Improved error messages in conflict resolution
```

## Getting Help

### Questions?

- **GitHub Discussions**: For questions and help
- **Discord**: (Coming soon)
- **Email**: dev@zaicodelabs.co.za

### Stuck?

Don't hesitate to:
- Ask in your PR
- Post in Discussions
- Tag maintainers (@yourusername)

### Reviewing PRs

Want to help review?
- Look for `needs review` label
- Check code quality
- Test changes locally
- Leave constructive feedback

## Recognition

Contributors will be:
- Listed in README.md contributors section
- Mentioned in release notes
- Eligible for ZaiCode Labs swag (coming soon!)

## License

By contributing, you agree that your contributions will be licensed under the MIT License.

---

**Thank you for contributing to ZaiGit! **

Questions? Open an issue or discussion!
