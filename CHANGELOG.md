# Changelog

All notable changes to ZaiGit will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Planned
- UI color customization
- Git hooks integration
- Bulk commit operations
- Interactive rebase support
- GitHub CLI integration
- Multi-repository management

## [1.0.0] - 2026-03-20

### Added

#### Core Features
- **Smart Push**: Automated push with AI-generated commit messages
  - Auto-pull before push (configurable)
  - Conflict detection and resolution
  - Interactive push preview
  - Confirmation prompts
- **Smart Pull**: Intelligent pull with conflict detection
- **Repository Status**: Detailed status with file categorization
- **Branch Operations**: Full branch management (list, create, switch, delete)
- **Commit History**: View last 10 commits with details
- **Stash Management**: Create, apply, and list stashes
- **Undo Last Commit**: Soft reset to previous commit
- **Sync Repository**: One-command sync (pull + push if needed)
- **Settings Management**: Configure all options via interactive menu

#### AI Integration
- **Ollama Integration**: Local AI for commit message generation
  - Support for multiple models (codellama, llama2, mistral, phi)
  - Conventional commit format enforcement
  - Streaming response handling
  - Configurable URL and model selection
- **AI Conflict Explanation**: AI-assisted conflict resolution with explanations
- **Smart Message Validation**: Validates and formats AI-generated messages

#### Configuration System
- **Persistent Configuration**: JSON-based config in `~/.zaigit/config.json`
- **Feature Toggles**: Enable/disable features individually
  - AI commit messages
  - Push confirmation
  - Auto-pull before push
- **Ollama Configuration**: URL and model settings
- **Git Preferences**: Customizable Git behavior

#### Conflict Resolution
- **Interactive Conflict Resolver**: Step-by-step conflict resolution
  - Keep ours/theirs options
  - Manual editing support
  - AI-powered explanations
- **Batch Conflict Resolution**: Handle multiple conflicts in sequence
- **Automatic Staging**: Stage resolved files automatically

#### User Interface
- **Branded Terminal UI**: Custom colors and styling
- **Progress Indicators**: Visual feedback for long operations
- **Detailed Previews**: Comprehensive change previews before operations
- **Color-Coded Output**: 
  - Green for additions
  - Yellow for modifications
  - Red for deletions
  - Blue for info messages
- **Interactive Menus**: Easy navigation through options
- **Emoji Support**: Visual indicators throughout UI

#### Developer Experience
- **Command Mode**: Direct command execution without menu
- **Interactive Mode**: Full-featured menu interface
- **Custom Messages**: Override AI with `-m` flag
- **Help System**: Built-in help and documentation
- **Version Display**: Check installed version

### Technical Improvements

#### Git Operations
- Accurate unpushed commit detection
- Proper diff generation for AI analysis
- Complete status information with all file states
- Conflict detection and file listing
- Branch force delete support
- Staged file management

#### Error Handling
- Comprehensive exception handling throughout
- User-friendly error messages
- Graceful fallbacks for AI failures
- Connection retry logic
- Validation for all user inputs

#### Code Quality
- Service-oriented architecture
- Dependency injection via Spring Boot
- Separation of concerns
- Package-private access for internal APIs
- Proper encapsulation

### Documentation
- Comprehensive README
- Detailed installation guide
- Complete user guide (HELP.md)
- Security policy (SECURITY.md)
- Contributing guidelines
- Code of Conduct
- This changelog

### Build & Distribution
- Maven build system
- Spring Boot integration
- Executable JAR distribution
- Installation script for Linux/macOS
- GitHub Actions CI/CD
- Automated releases

### Dependencies
- **Spring Boot** 3.2.3: Application framework
- **JGit** 6.8.0: Git operations
- **Jackson** 2.16.1: JSON processing
- **Java** 21: Runtime requirement

## [0.9.0-beta] - 2026-03-15

### Added
- Initial beta release
- Basic Git operations (push, pull, status)
- Simple menu interface
- Manual commit messages

### Known Issues
- getDiff() returned hardcoded string
- getUnpushedCommits() showed all commits, not unpushed
- File classification logic was incorrect
- Ollama streaming not fully implemented
- ConflictResolverService was stubbed
- No configuration persistence

## Version History Summary

| Version | Date       | Highlights |
|---------|------------|------------|
| 1.0.0   | 2026-03-20 | First stable release with AI integration |
| 0.9.0β  | 2026-03-15 | Initial beta release |

## Upgrade Guide

### From 0.9.0-beta to 1.0.0

#### Breaking Changes
None - 1.0.0 is fully backward compatible with 0.9.0-beta.

#### New Features to Configure
1. **Ollama Integration** (optional):
   ```bash
   zai-git → Settings → Configure Ollama
   ```

2. **Feature Toggles**:
   ```bash
   zai-git → Settings → Toggle Features
   ```

#### Configuration Migration
- Beta versions had no config file
- 1.0.0 creates `~/.zaigit/config.json` automatically
- Default settings preserve beta behavior

## Release Notes

### v1.0.0 - "Smart Start"

**Release Date**: March 20, 2026

ZaiGit 1.0.0 is the first stable release, bringing AI-powered Git automation to developers everywhere. This release focuses on making Git operations faster, safer, and more intelligent.

**Key Highlights:**

🤖 **AI-Powered**: Local AI via Ollama generates meaningful commit messages
🔒 **Safe Operations**: Confirmation prompts and detailed previews prevent mistakes
⚡ **Fast Workflow**: Smart push/pull/sync commands streamline daily Git tasks
🎨 **Beautiful UI**: Color-coded, emoji-enhanced terminal interface
⚙️ **Configurable**: Customize behavior to match your workflow
🛡️ **Conflict Resolution**: Interactive resolution with AI assistance

**What's Different from Beta:**
- ✅ Complete implementation (no more stubs!)
- ✅ Persistent configuration
- ✅ AI integration with Ollama
- ✅ Conflict resolution system
- ✅ Branch management (including delete)
- ✅ Comprehensive error handling
- ✅ Full documentation

**Installation:**
```bash
wget https://github.com/ZaiCodeLabs/zai-git/releases/download/v1.0.0/zai-git.jar
./install.sh
```

**Requirements:**
- Java 21+
- Git
- Ollama (optional, for AI features)

**Known Limitations:**
- No Windows native installer (use PowerShell script)
- No GUI version
- Single repository at a time
- No Git LFS support

**Coming Next:**
- v1.1.0: UI customization and Git hooks
- v1.2.0: Multi-repository support
- v2.0.0: GUI version

## Security Updates

All security updates are documented here. For security policy, see [SECURITY.md](SECURITY.md).

### Security Fixes in 1.0.0
- Proper input validation throughout
- Safe file operations with path validation
- Secure configuration file permissions
- No credential storage or transmission

## Support

### Getting Help
- **Documentation**: [HELP.md](HELP.md)
- **Issues**: https://github.com/ZaiCodeLabs/zai-git/issues
- **Discussions**: https://github.com/ZaiCodeLabs/zai-git/discussions
- **Email**: support@zaicodelabs.co.za

### Reporting Bugs
See [CONTRIBUTING.md](CONTRIBUTING.md) for bug report guidelines.

### Feature Requests
Open an issue with the `enhancement` label.

## Links

- **Homepage**: https://zaicodelabs.co.za
- **Repository**: https://github.com/ZaiCodeLabs/zai-git
- **Releases**: https://github.com/ZaiCodeLabs/zai-git/releases
- **Documentation**: https://github.com/ZaiCodeLabs/zai-git/tree/main/docs

## Contributors

Thank you to everyone who contributed to ZaiGit!

### Core Team
- **ZaiCode Labs** - Initial development and maintenance

### Contributors
<!-- Will be updated as contributors join -->

Want to contribute? See [CONTRIBUTING.md](CONTRIBUTING.md)!

## License

ZaiGit is released under the [MIT License](LICENSE).

---

**Changelog Format**: Based on [Keep a Changelog](https://keepachangelog.com/)  
**Versioning**: Based on [Semantic Versioning](https://semver.org/)

**Last Updated**: March 20, 2026
