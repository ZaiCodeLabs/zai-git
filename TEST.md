---

## **TEST.md** (Startup + User Verification Guide)

````markdown id="7c2m4x"
# Testing ZaiGit

Verify that ZaiGit is functioning correctly before using it in production repositories.

---

## Quick Test (Approx. 30 seconds)

```bash
# 1. Create a test repository
mkdir zai-test && cd zai-test
git init
echo "# Test Repo" > README.md
git add README.md
git commit -m "Initial commit"

# 2. Verify ZaiGit basics
zai-git status
zai-git log

# 3. Test smart push
echo "test change" >> README.md
zai-git push
````

---

## Complete Test Suite

### 1. Basic Operations

```bash
zai-git status
zai-git log
zai-git branches
```

---

### 2. Smart Push Workflow

```bash
echo "feature 1" >> README.md
zai-git push
```

Expected flow:

* Change detection
* AI-generated commit message
* Preview before confirmation

---

### 3. Sync and Pull

```bash
zai-git sync
```

---

### 4. Branch Operations

```bash
zai-git branches   # Create a branch (e.g., test-branch)
zai-git push       # Push changes
zai-git branches   # Switch branches
zai-git branches   # Delete branch
```

---

### 5. Stash Operations

```bash
echo "stash test" >> README.md
zai-git stash
zai-git status
zai-git stash pop
```

---

### 6. Undo Last Commit

```bash
git add README.md
git commit -m "test undo"
zai-git undo
```

---

## Expected Output Examples

### Clean Status

```
Repository Status
═══════════════════
Branch: main
Unpushed commits: 0
No changes to commit
```

### Changes Detected

```
Repository Status
═══════════════════
Branch: main
Added: README.md
Unpushed commits: 0
```

### Smart Push Preview

```
Smart Push
═════════════
Analyzing changes with AI...
AI Generated: test: add test content

File Changes:
  Modified (1 file):
    - README.md

Push to remote? (Y/n):
```

---

## Troubleshooting

| Issue                         | Solution                                            |
| ----------------------------- | --------------------------------------------------- |
| `command not found`           | Run `source ~/.bashrc` or restart terminal          |
| `Not a git repository`        | Ensure you are inside the test directory            |
| `Java 21+ required`           | Install Java 21 (see INSTALLATION.md)               |
| `remote hung up unexpectedly` | Skip remote tests or configure HTTPS authentication |

---

## Developer Testing

```bash
# Run unit tests
./mvnw test

# Build project
./mvnw clean package -DskipTests

# Build distributable
./install.sh --build
```

---

## Test Checklist

* [ ] `zai-git status` shows correct output
* [ ] `zai-git log` displays commit history
* [ ] `zai-git push` shows preview before execution
* [ ] Branch operations function correctly
* [ ] Stash operations work as expected
* [ ] `zai-git undo` reverts last commit
* [ ] Project builds successfully

---

## Optional: Remote Repository Test

```bash
# Add remote repository
git remote add origin https://github.com/YOUR_USERNAME/zai-test.git
git push -u origin main

# Test remote sync
zai-git sync
```

---

## Support

Email: [info@zaicodelabs.co.za](mailto:info@zaicodelabs.co.za)

