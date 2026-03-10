
# Notes
Durations:
Session - 1 year (absolute), 30 days (last activity)
Refresh Token - 7 days (if unused), rotating
Access Token - 30 minutes

TODO:
Refresh Flow:
1. We should check first the validity of the refresh token
	- a token is valid if expiresAt is not before now
	- revoked_at == null
		- if not null we trigger to revoke (use) all the tokens in the DB
	- If a token is not valid anymore. Force re-login but we will use the same session.

---

You will end up with these tables:
```
users
user_devices
sessions
refresh_tokens
```

Relationship:
```
User
 └── UserDevice (browser/device)
        └── Session (login instance)
               └── RefreshToken
```


RefreshToken and Session
![[Pasted image 20260309182135.png]]

// Validation  
// session.getStatus() == SessionStatus.ACTIVE  
// && session.getRevokedAt() == null  
// && session.getExpiresAt().isAfter(Instant.now())

# 0. To add

- [ ] refresh_token_reuse_detection
	- [ ] we will have a usedAt attribute. When refreshing we will check if the refresh token is used. 
```
Login (Day 1)
  └── generate familyId = UUID.randomUUID()   ← born here, never changes
  └── RefreshToken #1  familyId = "abc-123"

Refresh (Day 30)
  └── RefreshToken #2  familyId = "abc-123"   ← same

Refresh (Day 60)
  └── RefreshToken #3  familyId = "abc-123"   ← same

Logout / new login
  └── new familyId = UUID.randomUUID()         ← fresh chain
```
	
```
// On login — generate fresh familyId
RefreshToken token = RefreshToken.builder()
    .token(generateSecureToken())
    .session(session)
    .familyId(UUID.randomUUID())        // ← born here
    .expiresAt(session.getExpiresAt())
    .build();

// On rotation — carry it over
RefreshToken newToken = RefreshToken.builder()
    .token(generateSecureToken())
    .session(newSession)
    .familyId(old.getFamilyId())        // ← carried over
    .expiresAt(old.getExpiresAt())
    .build();
```

```
// Someone reused an old token — kill the entire login chain
@Modifying
@Query("UPDATE RefreshToken r SET r.usedAt = CURRENT_TIMESTAMP " +
       "WHERE r.familyId = :familyId AND r.usedAt IS NULL")
void revokeAllByFamilyId(@Param("familyId") UUID familyId);
```


- [ ] session_idle_timeout
- [ ] device_tracking
- [ ] IP change detection
- [ ] geo location detection
- [ ] suspicious login alerts
- [ ] device trust system
- [ ] remember this device
- [ ] session families
- [ ] refresh token hashing
- [ ] login rate limiting
- [ ] brute force protection
- [ ] account lockout after N failures
- [ ] concurrent session limit
- [ ] global logout
- [ ] revoke all sessions endpoint
- [ ] user session activity logs
- [ ] token binding
- [ ] anomaly detection
- [ ] login notification email
- [ ] session_families
- [ ] auth_audit_logs
- [ ] login_attempt_logs
- [ ] security_risk_score
- [ ] mfa_challenges
- [ ] device_fingerprinting
	- [ ] Check: Pre-authentication (Risk Assessment)
		- **When**: At the very start of the login request, before you even verify the password.
		- **What to Check**: Compare the current fingerprint against known **fraudulent device databases** or "bot-like" patterns (e.g., mismatch between User-Agent and TLS fingerprint).
		- **Action**: If the risk score is too high, you can block the request immediately or present a CAPTCHA before allowing the credential check.
	- [ ] Check: Primary Auth (Decision Engine)
		- **When**: Immediately after verifying the user's password.
		- **What to Check**: Compare the incoming fingerprint hash against the **stored trusted fingerprints** associated with that specific user ID.
		- **Actionable Decisions**:
		    - **Known Device**: Proceed with the login and issue the session/JWT.
		    - **New/Unknown Device**: Do **not** issue a full-access token. Instead, trigger a "Step-up" authentication (MFA/email code).
		    - **Suspicious Change**: If the fingerprint matches but the IP/Location has shifted drastically, flag the session for manual review or extra verification
	- [ ] Post Auth: Session Integrity
		- **When**: On every subsequent sensitive API call (e.g., /withdraw, /change-password)
		- **What to check**: Verify that the fingerprint attached to the active session hasn't changed.
		- **Action**:If a session originally started on a Chrome/Windows device suddenly sends a request from a Safari/iPhone fingerprint, **terminate the session** immediately as it likely indicates session hijacking or token theft.