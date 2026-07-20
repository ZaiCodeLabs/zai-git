# Testing ZaiGit

This guide covers automated tests and manual verification of the command line application.

## Automated Verification

Run the complete Maven verification lifecycle:

```bash
./mvnw verify
```

This command compiles the application, runs the test suite, and creates the executable application at `target/zai-git.jar`.

## Manual Verification Repository

Use a temporary repository so that testing does not affect production work:

```bash
mkdir zai-git-test
cd zai-git-test
git init
git config user.name "ZaiGit Test"
git config user.email "test@example.com"
echo "Test repository" > README.md
git add README.md
git commit -m "test: initialize repository"
```

## Status and History

```bash
zai-git status
zai-git log
```

Confirm that the current branch, working tree state, and recent commit are displayed correctly.

## Commit and Push Workflow

Remote operations require a test remote with valid authentication:

```bash
git remote add origin <test-repository-url>
git push -u origin HEAD
echo "Test change" >> README.md
zai-git push
```

Confirm that ZaiGit detects the change, presents an appropriate commit message, displays the affected file, and requests confirmation when that setting is enabled.

## Pull and Synchronization

```bash
zai-git pull
zai-git sync
```

Confirm that both commands report failures with a nonzero process status when the remote operation cannot be completed.

## Stash Workflow

```bash
echo "Stashed change" >> README.md
zai-git stash
zai-git status
zai-git stash pop
```

Confirm that the change is restored and that the applied stash is removed from the stash list.

## Undo Workflow

```bash
echo "Undo test" >> README.md
git add README.md
git commit -m "test: verify undo"
zai-git undo
git status
```

Confirm that the latest commit is removed while its changes remain staged.

## Interactive Workflows

Run `zai-git` without arguments and verify the following menu operations:

* List, create, switch, and delete branches
* View repository status and history
* List and apply stashes
* Update application settings
* Detect and inspect merge conflicts

Use disposable branches and files for destructive workflow testing.

## Verification Checklist

* [ ] `zai-git status` reports added, modified, and deleted files accurately
* [ ] `zai-git log` displays recent commit history
* [ ] `zai-git push` can push existing local commits when the working tree is clean
* [ ] A failed push does not discard the local commit
* [ ] `zai-git stash pop` applies and removes the latest stash
* [ ] `zai-git undo` retains the changes from the removed commit
* [ ] Unknown commands return a nonzero process status
* [ ] `zai-git version` matches the packaged project version
* [ ] `./mvnw verify` succeeds

## Support

Report reproducible defects through [GitHub Issues](https://github.com/ZaiCodeLabs/zai-git/issues).
