Basic Custom Authentication (Token-based/JWT) with Opaque Refresh Token (Token Rotation)

--- 
# TODO
- [ ] **Multi-user per device, multi-device per user**
	- [x] Create Device Entity
	- [x] Refresh Token Revocation
	- [ ] Rotation & Invalidation: Use Refresh Token Rotation so that using an old token (e.g., by an attacker) automatically invalidates the entire family of tokens.
	- [ ] DeviceCookie shouldn't always be returned 
	- [ ] Device session id implementation
	- [ ] What happens to refresh token when another login happens? 
	- [ ] When user keeps logging in the tokens are being stored 
	- [ ] Implementation of family_id (token family/rotation)
	- [ ] Race conditions when two tabs request for new refreshtokens at the same time
	- [ ] Logging out Scenarios
		- [ ] Soft Logout
		- [ ] Logout on one device
		- [ ] Logout on all devices
	- [ ] Absolute lifetime of token expiry
		- [ ] Store `created_at` and `absolute_expiry` fields.
		- [ ] When issuing new refresh tokens, **copy the same `absolute_expiry`** to the new token.

---

# General Notes

---
# Daily Development Artifacts