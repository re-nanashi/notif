##### Description:
Commit messages are important for keeping your project history clear and professional.  
Follow these best practices for every commit.

---

## 1. Structure Your Commit Message

Format:

```
<type>: <short summary>

[optional longer description]
```

### Common Types
- `feat:` → new feature  
- `fix:` → bug fix  
- `chore:` → build tasks, configs, maintenance  
- `docs:` → documentation updates  
- `style:` → formatting, missing semicolons, etc. (no code changes)  
- `refactor:` → code changes that neither fix a bug nor add a feature  
- `test:` → adding or updating tests  
- `perf:` → performance improvements  

---

## 2. Use Present Tense, Imperative Mood

✅ Good:  
- `fix: update navbar link styles`  
- `feat: add user login page`  

❌ Bad:  
- `fixed navbar links`  
- `adding login page`  

---

## 3. Keep Summary Short
- First line ≤ 50 characters  
- Capitalize the first letter  
- No period at the end  

---

## 4. Add Body for Complex Commits
If needed, leave a blank line after the summary and explain more:

```
feat: add user authentication

- Implement JWT-based login
- Add password hashing with bcrypt
- Update API routes with auth middleware
```

---

## 5. Reference Issues (Optional)

If working with GitHub Issues/Jira:

```
fix: resolve login redirect bug (#123)
```

---

## 6. Avoid Generic Messages

❌ Bad:
- `update code`
- `fix stuff`
- `misc changes`

✅ Good:
- `fix: correct payment total calculation`
- `chore: upgrade dependencies to latest versions`

---

## 7. One Purpose Per Commit
- Each commit should represent **one logical change**  
- Don’t mix unrelated fixes/features in one commit  

---

## ✅ Quick Examples

```
feat: add user profile page
fix: correct navbar z-index issue
docs: update API usage section in README
chore: configure ESLint and Prettier
refactor: simplify cart reducer logic
```

---

## 👉 Rule of Thumb
Commit like you’re writing a **changelog entry** for your future self (or your teammates).

# 📑 Commit Message Types (Conventional Commits)

Use these prefixes to keep commit history clear and consistent.

| Type          | When to Use                                                      | Example Commit Message                      |
| ------------- | ---------------------------------------------------------------- | ------------------------------------------- |
| **feat:**     | A new feature or functionality                                   | `feat: add calendar view to track workouts` |
| **fix:**      | A bug fix                                                        | `fix: correct total workout count logic`    |
| **docs:**     | Documentation changes (README, guides, API docs)                 | `docs: add setup instructions to README`    |
| **style:**    | Code style/formatting (no logic changes)                         | `style: format code with Prettier`          |
| **refactor:** | Code restructuring without changing behavior                     | `refactor: simplify workout logging logic`  |
| **perf:**     | Performance improvements                                         | `perf: optimize query for workout history`  |
| **test:**     | Adding or updating tests                                         | `test: add unit test for workout service`   |
| **chore:**    | Maintenance tasks, dependencies, configs, small tweaks           | `chore: update dependencies`                |
| **ci:**       | Continuous integration / pipeline changes                        | `ci: add GitHub Actions workflow`           |
| **build:**    | Build system or external dependencies (npm, Docker, Maven, etc.) | `build: upgrade to Node.js 20`              |
| **revert:**   | Reverting a previous commit                                      | `revert: undo calendar view feature`        |

---

✅ **Rule of thumb for README updates:**
- Big updates → `docs:`
- Small fixes/tweaks → `chore:`