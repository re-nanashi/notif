##### Description:
Best Practices for Branch naming for Github Flow 

---
## 🔑 Types (Prefixes)

- `feature/...` → new features  
- `fix/...` → bug fixes  
- `chore/...` → maintenance, configs, refactoring  
- `docs/...` → documentation changes  
- `test/...` → experiments or testing  

---
## 📝 Examples

✅ Good:
- `feature/add-user-auth`
- `fix/login-bug`
- `chore/update-dependencies`
- `docs/update-readme`

❌ Bad:
- `feature/stuff`
- `fix/issue1`
- `mybranch`
- `new-feature`

---
## 📌 Best Practices

- Use **lowercase** and **hyphens** (`-`) for readability  
  - ✅ `feature/add-payment-api`  
  - ❌ `feature/AddPaymentAPI`  
  - ❌ `feature/add_payment_api`  

- Keep branch names **short and descriptive** (2–5 words)  
  - ✅ `fix/navbar-overlap`  
  - ❌ `feature/add-the-new-super-complicated-user-authentication-system`  

- (Optional) Add task/issue IDs if using project management tools  
  - `feature/123-add-user-auth`  
  - `fix/456-payment-timeout`  

- Delete branches after merging into `main` (keeps repo clean)

---