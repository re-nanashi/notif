
# 🧱 Phase 1 — Minimal, Correct, Production-Safe (Start Here)

### 🎯 Goal:

- One user
- Multiple devices allowed
- No multi-account-per-device yet
- Per-device logout supported
- Clean upgrade path later

---

## ✅ Step 1 — Basic Refresh Token Table

Create this:
```
refresh_tokens
-------------------------------------------------------
id (uuid)
user_id
token_hash
device_id
expires_at
revoked (boolean)
created_at
```

That’s it. No extra complexity.
Why include `device_id` already?
Because:
- It costs nothing
- Prevents refactor later
- Enables per-device control

---

## ✅ Step 2 — On Login

Flow:
1. Check if `device_id` cookie exists
2. If not → generate UUID → set cookie `device_id`
3. Generate refresh token
4. Hash it
5. Delete tokens for:
     ``` WHERE user_id = ? AND device_id = ?```
6. Insert new token row
7. Send refresh token cookie (HttpOnly, Secure)

This gives you:
✔ Multiple devices  
✔ Only one session per device  
✔ Re-login refreshes device session  
✔ No global logout

That’s already solid architecture.

---

## ✅ Step 3 — On Refresh

1. Read refresh token from cookie
2. Hash it
3. Find matching row
4. Verify:
    - Not revoked
    - Not expired
5. Issue new access token

(Optional but recommended: rotate refresh token)

---

## ✅ Step 4 — Logout Current Device

```
UPDATE refresh_tokens  
SET revoked = true  
WHERE user_id = ? AND device_id = ?
```

Only this device logs out.

Other devices remain active.

---

# 🚫 What You Should NOT Build Yet

Do NOT build yet:

- Multi-account-per-device
- Device names (MacBook Pro, iPhone, etc.)
- Suspicious login detection
- Device fingerprinting
- Global device session container

That’s Phase 3+.

---

# 🧠 Phase 2 — Add Global Logout

Add endpoint:
```
UPDATE refresh_tokens  
SET revoked = true  
WHERE user_id = ?

Now user can “Log out of all devices”.

Still lean.
```


---

# 🧠 Phase 3 — Multi-Account Per Device

ONLY when you want account switching:

Then you introduce:
```
device_sessions table  
user_sessions table
```

But don’t start there.

---

# 🧭 The Lean Implementation Order

Here is the correct order:

### 1️⃣ Single login works

### 2️⃣ Add refresh token hashing

### 3️⃣ Add device_id column

### 4️⃣ Support multiple devices

### 5️⃣ Add per-device logout

### 6️⃣ Add global logout

### 7️⃣ (Later) Add multi-account per device

Each builds naturally on the previous one.

---

# 🔥 Why This Is The Best Path

Because:
- You avoid rewriting DB schema
- You avoid early complexity
- You keep security strong
- You build features in logical order
- You don't build features you may never need

---

# 🎯 Important Design Decision

You already made the correct one:

> Revoke only tokens for the device logging in.

That’s exactly how modern systems behave.