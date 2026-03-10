Authentication Flow Implementation with basic account security.

---
# TODO
- [ ] **Registration Flows**
	- [x] Custom email and password validation/validators
	- [x] [Activating a new account by email](https://www.baeldung.com/registration-with-spring-mvc-and-spring-security)
	- [ ] [Registration Series](https://www.baeldung.com/spring-security-registration)
		- [x] Email Verified private boolean emailVerified;
	- [ ] Spring Configuration [refer here] ()
	- [ ] Resend verification email
		- [ ] Rate Limiting: Apply strict per-IP and per-email limits (e.g., maximum 3 resends per hour) to prevent your mail server from being used for harassment. **429** Rate Limit
		- [ ] Cooldown Timers: Implement a "wait 60 seconds before trying again" rule on the backend, even if the frontend button is disabled.
		- [x] Verification Token Invalidation: Ensure that generating a new token immediately **voids all previous tokens** associated with that account to prevent replay attacks.
- [ ] **Basic Account Security**
	- [ ] Invalid Credentials Handling
	- [ ] Brute force Protection
	- [ ] Access Token (short-lived)
	- [ ] Attack Vectors
- [ ] **Recovery Flows**
	- [ ] Forgot password
	- [ ] Reset password tokens
	- [ ] expired reset llinks
	- [ ] email verification resend
	- [ ] account recovery
- [ ] **Account Security**
	- [ ] Failed login counter
	- [ ] Account lockout 
	- [ ] CAPTCHA
	- [ ] MFA/2FA
	- [ ] OTP
	- [ ] Email Codes
	- [ ] password history / No reuse of old passwords
	- [ ] Password expiry / Rotation
	- [ ] What if account is unverified?
		- What if account is unverified, and I tried to change the email, what will happen to the token?
	- [ ] changeEmail should trigger re-verification
		- [ ] set email to new email -> set enabled to false -> save -> generateVerificationToken

--- 
# General Notes

---
# Daily Development Artifacts