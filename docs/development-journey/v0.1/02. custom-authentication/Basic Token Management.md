Basic Custom Authentication (Token-based/JWT) with Opaque Refresh Token (Token Rotation).

--- 
# TODO

- [x] **Auth Module Basic Implementation**
	- [x] Authentication Service
	- [x] Authentication Controller
		- [x] Fix all custom exceptions; make sure it is properly structured
		- [x] Reduce custom exceptions; Generic exception encompassing other entities + Specific ErrorCodes
		- [x] Remove errormappers and just implement it directly in the ControllerAdvice. Make each method specific for each exception
	- [x] Check which methods a normal user can access
	- [x] Implement SecurityConfiguration for securing endpoints.
	- [x] Implement auth/me
	- [x] Refactor ApiError and ApiValidationError
	- [x] Implement AuthenticationEntryPoint 
	- [x] Register user through auth (not valid did not get caught)
	- [x] Implement simple roles
		- [x] Data Initializer for Admin 
	- [x] Test endpoints and assumptions
- [x] **Basic Token Management (JWT/Opaque)**
	- [x] Issue access tokens
		- [x] Get currently logged in user
	- [x] Issue refresh tokens through HttpOnlyCookie
		- [x] Create RefreshToken entity
		- [x] Create RefreshTokenRepository
		- [x] Create RefreshTokenService
		- [x] Utility: Create CookieUtil
		- [x] Update AuthService login()
		- [x] Commit Changes
		- [x] Comment AuthService
		- [x] Comment AuthController Advice
		- [x] Move Client to api
		- [x] Move CreateUserRequest to API
		- [x] Implement AuthService refresh()
			- [x] Test first if we got the cookie
			- [x] Test user needs to to be able to refresh when access token is expired.
		- [x] Implement AuthService logout()
			- [x] On-Demand Revocation: Ensure your Logout functionality explicitly calls a backend API to delete the token from the database, rather than just clearing the cookie on the frontend
		- [x] Controller: Add /auth/refresh endpoint
			- [x] Cookie Path ("/") > "/api/v1/auth/refresh" only; since we are using the `__Host-` prefix, we really can't change the path where we send the cookie.
		- [x] Controller: Add /auth/logout endpoint
		- [x] Exception handling: Add token-related exceptions
		- [x] Security Config: Whitelist refresh and logout endpoints
		- [x] Fix ApiResponse; Object -> T
		- [x] [Salting and Hashing JWT/Signing](https://medium.com/@berto168/salt-pepper-spice-up-your-hash-b48328caa2af)
		- [x] Check transactional methods (if should be atomic or not)

---
# General Notes

---
# Daily Development Artifacts