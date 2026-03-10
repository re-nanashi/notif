
---
# TODO
- [x] Revise README.md so that it shows the changelogs and features of my dummy application and also the Architecture of the code.
- [ ] [Security Best Practices](https://curity.io/resources/learn/jwt-best-practices/)
	- [ ] Rotate the secret key (gracefully handle rotation)
- [ ] Implement JWT; Spring Security
	- [x] Implement User Management System (Currently at **UserController.java**)
		- [x] **UserController.java**: [Implement](https://www.baeldung.com/spring-valid-vs-validated) valid annotation for the DTOs
			- [x] Check if all routes are working properly
				- [x] GET /api/v1/users; Get all users.
				- [x] GET /api/v1/users/2; Get user with ID 2 and test exceptions.
				- [x] POST /api/v1/users; Create new user
					- [x] Create new user
			- [x] Check if all DTO validations are properly implemented
				- [x] For input, there should be min max for names
			- [x] updateUser()
				- [x] Just make sure that updateUser works as intended; Check valid/invalid input. Postpone business rules.
				- [x] [Future Implementation] Not all variables are empty; there should be 1 or 2 or all variables are going to be changed
				- [x] [Future Implementation] If one variable on the updateRequest is already set, cancel update and throw the exception. 
			- [ ] changePassword()
		- [x] **UserService.java**: Reformat that exceptions messages to be more robust. As we already have the generic or more general messages on the  Exception Handlers
			- [x] Implement use of error codes; Sample: "User not found" -> "USER_NOT_FOUND" 
		- [x] Implement UserService class 
			- [x] getAllUsers(admin-only) 
		- [x] Implement UserController (include admin-only methods)
			- [x] Admin only: Get all users, create user 
		- [x] Change branch to feature/user-management; Fix the validation code for the DTOs
			- Password Regex Pattern
			- @Valid annotation 
			- [DTO-level validation](https://medium.com/paysafe-bulgaria/springboot-dto-validation-good-practices-and-breakdown-fee69277b3b0)
			- [Layers of validation](https://stackoverflow.com/questions/16394296/in-which-layer-should-validation-be-performed)
			- [x] CreateUserRequest.java
			- [x] UpdateUserRequest.java
			- [x] ChangeEmailRequest.java
			- [x] ChangePasswordRequest.java
		- [x] Error mapper
			- [x] Create an Error mapper feature local and global
			- [x] Error handling will now be on feature local ExceptionHandlers
			- [x] How to implement Single errors and multiple errors
	- [ ] Auth Server (Token-based/JWT)
		- [x] Basic JWT without RefreshToken
			- [x] We need to implement the Authentication service
			- [x] Authentication Controller (Check which methods and messages can an unauthenticated user can access/view)
				- [x]  Check ControllerAdvices. Q: How to make the structure coherent?
					- My Problem: There are too many *related* exceptions, yet they are too nuanced. My initial approach was to create multiple custom exceptions, but it's too much of a hassle to 
				- [x] Fix all custom exceptions; make sure it is properly structured
					- [x] Reduce custom exceptions; Generic exception encompassing other entities + Specific ErrorCodes
				- [x] Remove errormappers and just put it in the controlleradvice. Make each method specific for each exception
				- [x] [Current] Try to build first, add comments, push to github
			- [x] Check which methods a normal user can access
			- [x] Implement SecurityConfiguration for securing endpoints.
			- [x] Implement auth/me
			- [x] Refactor ApiError and ApiValidationError
			- [x] Implement AuthenticationEntryPoint 
			- [x] Register user through auth (not valid did not get caught)
			- [x] Implement simple roles
				- [x] Data Initializer for Admin 
			- [x] Test endpoints and assumptions
		- [x] **User Identity Management**
			- [x] create users
			- [x] update profile
			- [x] delete users
			- [x] unique identifiers (email/username)
			- [x] password hashing (bcrypt/argon2)
			- [x] password policies
			- [x] fix: When user is already registered, it doesn't say.
			- [x] token should indicate if used, should it be removed? 
			- [x] Check for .equals method cause the calling object might be null
			- [x] Service should have an interface
			- [x] Implement BaseEntity.java
			- [x] Remove autowiring. 
			- [x] What?? It sent a account disabled first even though the password was wrong? jfabro3124@gmail.com
			- [x] AuthenticatedUser -> LoginResponse
			- [x] Fix exceptions
			- [x] Fix User Client should bubble up the error
			- [x] DTO should indicate if account is active
			- [x] RegisterResponse
			- [x] VerificationTokenRepository
			- [x] Rename: RegistrationListener, CreateUserRequest, client, UserDTO, 
			- [x] TokenExceptions
			- [x] Add this to error codes [SERVICE]_[DEPENDENCY]_[PROBLEM]
		- [ ] **Token Management (JWT/opaque)**
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
			- [x] [Secure1](https://medium.com/@berto168/salt-pepper-spice-up-your-hash-b48328caa2af)
			- [x] Salting and hasing JWT/signing
			- [x] Check transactional methods (if should be atomic or not)
			- [ ] [[Robust Auth Implementation]] + [[Multi-user per device, Multi-device per user,  refresh token implementation]]
				- [x] Create Device Entity
				- [x] Create Device 
				- [x] Revoke refresh token everylogin
				- [ ] Token revocation
				- [ ] DeviceCookie shouldn't always be returned 
				- [ ] Device session id implementation
				- [ ] What happens to refresh token when another login happens? 
				- [ ] Logging out
				- [ ] Race conditions multiple simultatnous logins
			- [ ] Update LocalDateTime.now() -> Instant.now()
			- [ ] refresh_token_rotation
				- [ ] When user keeps logging in the tokens are being stored 
				- [ ] Implementation of family_id (token family/rotation)
				- [ ] Rotation & Invalidation: Use Refresh Token Rotation so that using an old token (e.g., by an attacker) automatically invalidates the entire family of tokens.
				- [ ] Race conditions when two tabs request for new refreshtokens at the same time
			- [ ]  Absolute lifetime of token expiry
				- [ ] Store `created_at` and `absolute_expiry` fields.
				- [ ] When issuing new refresh tokens, **copy the same `absolute_expiry`** to the new token.
			- [ ] Do not call save if a jpa managed entity
			- [ ] Comment Exceptions in core-module
			- [ ] User should have a status to do soft delete first
			- [ ] Device batch delete if last seen was 90 days ago
			- [ ] VerificationToken should be hashed
		- [ ] **Authentication Flows**
			- [ ] Invalid Credentials Handling
			- [ ] Brute force Protection
			- [ ] Access Token (short-lived)
		- [ ] **Authorization Model**
			- [ ] [Roles](https://www.geeksforgeeks.org/advance-java/spring-security-role-based-authentication/)
			- [ ] Role-Based (RBAC) [Refer here](https://www.baeldung.com/role-and-privilege-for-spring-security-registration)
				- [ ] USER
				- [ ] ADMIN
				- [ ] MODERATOR
			- [ ] Attribute-based (ABAC)
				- [ ] user.department == "HR"
				- [ ] user.id == resource.ownerID
			- [ ] Scope-based (ABAC)
				- [ ] read:users 
				- [ ] write:orders
			- [ ] [Security](https://www.toptal.com/developers/spring/spring-security-tutorial)
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
		- [ ] **Recovery Flows**
			- [ ] Forgot password
			- [ ] Reset password tokens
			- [ ] expired reset llinks
			- [ ] email verification resend
			- [ ] account recovery
			- [ ] [Registration Series](https://www.baeldung.com/spring-security-registration)
				- [x] Custom email and password validation/validators
				- [x] [Activating a new account by email](https://www.baeldung.com/registration-with-spring-mvc-and-spring-security)
				- [ ] Resend verification email
					- [ ] **Rate Limiting:** Apply strict per-IP and per-email limits (e.g., maximum 3 resends per hour) to prevent your mail server from being used for harassment. **429** Rate Limit
					- [ ] **Cooldown Timers:** Implement a "wait 60 seconds before trying again" rule on the backend, even if the frontend button is disabled.
					- [x] **Token Invalidation:** Ensure that generating a new token immediately **voids all previous tokens** associated with that account to prevent replay attacks.
		- [ ] **Session Management**
			- [ ] List active sessions
			- [ ] logout single session
			- [ ] logout all sessions
			- [ ] device tracking
			- [ ] last login time
		- [ ] **Audit & Security Logs**
			- [ ] Login attempts
			- [ ] IP address
			- [ ] device info
			- [ ] token issued
			- [ ] password changed
			- [ ] MFA enabled
		- [ ] **Admin Capabilities**
			- [ ] Ban users
			- [ ] Reset passwords
			- [ ] Assign Roles
			- [ ] Disable accounts
			- [ ] Impersonate users
		- [ ] **Federation / Exeternal Identity (OAuth)**
			- [ ] login with Google
			- [ ] login with Github
			- [ ] LDAP / Active Directory
			- [ ] SAML / OIDC
		- [ ] **Standards Support**
			- [ ] OAuth2
			- [ ] OpenID Connect
			- [ ] PKCE
			- [ ] Client credentials flow
			- [ ] Authorization code flow
			- [ ] Token introspection endpoint
			- [ ] JWK endpoint
	- [ ] Core Module + Implement Logging
		- [ ] make changes on error messages. Just log to check internal errors. 
		- [ ] RequestIds inside Responses(?)
	- [ ] Authorization
		- [ ] Users should use /me for current user operations and /users/id for admin-level
		- [ ] Implement Roles
		- [x] Implement DataInitializer we should have 1 administrator 
		- [ ] Implement only being able to access own data [refer here](https://stackoverflow.com/questions/51712724/how-to-allow-a-user-only-access-their-own-data-in-spring-boot-spring-security)
		- [ ] Only connected users can update their own data (profile, password, etc). Admin can update anyone's profile.  
		- [ ] We should check the ID on the endpoint and check if the connected user has the same ID.
		- [ ] This is a multitenant app so we should look into MultiTenant application implementations. [[MultiTenant Ref]]
	- [ ] OAuth
		- [ ] OAuth needs RSA algorithm
	- [ ] Background processing
		- [ ] Background Cleanup: Run a scheduled job to delete expired tokens from your database to prevent bloat.
	- [ ] gRPC - Micro Services
		- [ ] Key rotation (JWT) - Better if we are already using Microservices.
		- [ ] What if there was an error for email service? (since we are using event-driven communication with email service)
		- [ ] https://www.youtube.com/watch?v=FDjmWxETjn8
		- [ ] Token Introspection
	- [ ] Domain Implementation
		- [ ] Add "instance": "path" to ApiError
		- [ ] User needs to have locked, disable functionality
		- [ ] Batch Processing delete voided verification tokens, and delete refresh tokens
	- [ ] Kafka for analytics
	- [ ] [[User Registration]]
		- [ ] [2-step registration](https://www.baeldung.com/spring-valid-vs-validated) using @Valid and @Validated
		- [ ] Email Verified private boolean emailVerified;
		- [ ] [refer](https://medium.com/@aprayush20/spring-security-authentication-configuration-demystified-e44be02322fb)
		- [ ] [Silent Authentication](https://auth0.com/docs/authenticate/login/configure-silent-authentication)
		- [ ] Logout on all devices.
	- [ ]  General Security Practices
		- [ ] Rate-limit / login
		- [ ] ValidationGroup @Validated{}
		- [ ] [[Sample Management Controller Endpoints]]
		- [ ] Implementing Role Repositories?
- [x] Fix (09-11-2025): dockerfile not building inside ./backend folder 
- [ ] Build, Break and Learn then Build again
	- [ ] your own auth system (JWT)
		- [ ] Security 
		- [ ] Crypto Basics
		- [ ] Tokens
		- [ ] Sessions
		- [ ] Attack vectors
		- [ ] Refresh Rotation
	- [ ] your own rate limiter / API Gateway
		- [ ] token bucket
		- [ ] sliding window
		- [ ] Redis
		- [ ] abuse detection
		- [ ] Auth offloading
		- [ ] Circuit breakers
		- [ ] Then
			- [ ] Kong
			- [ ] Nginx
			- [ ] Spring Cloud Gateway
	- [ ] your own in-memory cache (cache layer)
		- [ ] eviction policies
		- [ ] consistency
		- [ ] stale data
		- [ ] race conditions
		- [ ] Integrate Redis
			- [ ] Redis
			- [ ] TTL
			- [ ] Pub/sub
			- [ ] distributed locks
	- [ ] your own message queue(job queue?)
		- [ ] retries
		- [ ] idempotency
		- [ ] failure handling
		- [ ] DLQ
		- [ ] Senior 
			- [ ] RabbitMQ
			- [ ] Kafka
			- [ ] SQS
	- [ ] your own scheduler
	- [ ] your own load balancer
	- [ ] your own ORM
	- [ ] your own logger
- [ ] CI/CD - Github Actions
- [ ] AWS
- [ ] API Integration (Websockets)
- [ ] [Production-ready Management System with Microservices: Java Springboot AWS](https://www.youtube.com/watch?v=tseqdcFfTUY)
- [ ] Event processiong: Event Processing: Use SNS+SQS and Kafka for event-driven messaging and processing
- [ ] Implement better UI for emails.
- [ ] mitigate XFF errors on our edge Proxy, have a trusted list of proxies on our server
- [ ] Batch process Delete
```
// Scheduled job — run nightly
@Scheduled(cron = "0 0 2 * * *")  // 2am every day
@Transactional
public void cleanUpExpiredSessions() {
    sessionRepository.deleteByStatusAndLastActiveAtBefore(
        SessionStatus.REVOKED,
        Instant.now().minus(Duration.ofDays(30))
    );
}
```




--- 
## Notes: 
**AuthService**
- Handles **authentication, authorization, and token management**.
- Typical responsibilities:
    - login() → authenticate with username/password, generate JWT.
    - refreshToken() → issue new access token from refresh token.
    - validateToken() → check if JWT is valid.
    - logout() (depending on your approach).
	- Location: com.notif.api.auth (feature package).
	
**UserService**
- Handles **user profile & account management**.
- Typical responsibilities:
    - registerUser() → create new account.
    - updateUserProfile() → update personal info.
    - getUserById() → fetch details.
    - deleteUser() → remove account.
    - Location: com.notif.api.user.

**Validation**
- Format & basic constraints → DTO
- Business & security rules → Service

**User Registration**


# References
## Validation and Validation Errors
[How to handle validation errors in Spring Boot to become human-friendly](https://medium.com/@fingervinicius/how-to-handle-validation-errors-in-spring-boot-to-become-human-friendly-90bd2ec3ed6e)
[SpringBoot DTO Validation - Good practices and breakdown](https://medium.com/paysafe-bulgaria/springboot-dto-validation-good-practices-and-breakdown-fee69277b3b0)
[Differences in @Valid and @Validated Annotations in Spring](https://www.baeldung.com/spring-valid-vs-validated)
[Spring Boot Global Exception Handler](https://medium.com/@aedemirsen/spring-boot-global-exception-handler-842d7143cf2a)

# Notes
Access tokens -> App memory (Javascript) - Secure:XSS (mitigate with strict Content Security Policy, secure coding)
Refresh tokens -> HttpOnlyCookie + (server-side for revoke)

Person create an account ("User 1") 
Server gives 1 access token and 1 refresh token. 
Access token lasts for 15 minutes.
Refresh token lasts for 7 days. (Remind me options makes the token last 30 days.)

User1 uses access token to access private resources. 
Once access token expires, server checks client's cookie if refresh token is still valid or not (revoked or expired).

We just need to know if the refresh token is expired. If it is expired

TokenRepository and then we query by ID 

#### February 7, 2025
From our system's context (*implementation are all assumptions*)
AlreadyExistsException - When the object itself already exists.
ResourceConflictException - Same email, so i'm assuming this is thrown when a part is already in use.
IllegalStateException - Email is already set to this value.

Common Encountered Exceptions:
1. NotFoundExceptions
2. AlreadyExistsExceptions
3. ResourceConflictException

BadCredentialsException
**UsernameNotFoundException**

#### February 8, 2025
**UserService.java**
- throw new UserAlreadyExistsException("User with email '" + request.getEmail() + "' already exists");
- .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
- .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
- .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
- throw new IllegalArgumentException("No update values provided. At least one field must be changed.");
- 


#### February 8, 2025
**AuthenticationEntryPoint**
- AuthenticationEntryPoint.java
- ApplicationConfig.java
- SecurityConfig.java

#### February 8, 2025
```
if (env == DEV) {
   log.info("Verification link: {}", link);
} else {
   emailService.send(link);
}
```

#### February 18, 2025
For orchestration failures -> Application Exception create from Business exceptions
For repository failures -> Domai Exception From business exceptions


#### February 20, 2025

- [x] Check structure Structure
- [x] LoginRequest DTO
- [x] LoginResponse DTO
- [x] Handlers for user and auth. We need to make sure AuthEntryPoint throws a specific errorcode and not a generic one.
- [x] Check io.jsonwebtokens exceptions what they throw so we can rethrow and then we can handle it properly (know what type of error) in auth entry point
	- [x] Claims::getSubject
	- [x] Jwts.parserBuilder().setSignInKey(getSignInKey()).build().parseClaimsJws(token).getBody();
	- [x] Keys.hmacShakeyFor(KeyBytes);
	- [x] Jwts.builder()
	- [x] Claims::getExpiration
	- [x] Expired, Malformed, 
- [x] Include user credentials error in Auth Entry Point
- [ ] [Getting currently logged user](https://stackoverflow.com/questions/50803727/spring-with-jwt-auth-get-current-user)

**NOTES**:
- Filter exceptions trigger the AuthEntryPoint and subsequent calls also triggers the entry point
- So for example when logging in. Since the endpoint is unsecure. We can use it to login. Then once we try to login 
- my issue is implementation of handlers will be redundant.  For authenticating I use the Advice in the auth module throwing errors like Credentials expired (by the AuthenticationManager.authenticate() method). Now if let's say I was able to get the JWT token from logging in, and then I suddenly disable the user. That would mean I would still have access to the resources as long as JWT is valid even though user is disabled. This will be handled properly only if I create a custom logic in the OncePerRequestFilter (JWT custom Filter) and throwing BadCredentials or DisabledExceptions. That in turn would trigger the AuthEntryPoint
- Auth and Authorization will be handled by the AuthEntryPoint and Access Denied Handler (hasRole('ADMIN')) // hasROLE('USER') but not every user can access other's resources
- Security-Level Authorization
	- Only admins can access `/admin/**` -> http.authorizeHttpRequests().requestMatchers("/admin/**").hasRole("ADMIN") 
	- Only users with `ROLE_EDITOR` can access `/edit` -> @PreAuthorize("hasRole('EDITOR')")
- Business rules will be handled by @RestControllerAdvice
	- Role might be `USER`, but you need to check `order.ownerId == currentUserId`. Controller/service: `if (!order.getOwnerId().equals(currentUserId)) throw AccessViolationException;`
	- Role `MANAGER` is not enough; need to check `request.teamId == manager.teamId`. 
	
## 2️⃣ Common Subclasses

| Exception                  | When it occurs                                                   |
| -------------------------- | ---------------------------------------------------------------- |
| `ExpiredJwtException` ✅    | The JWT’s `exp` (expiration) date is in the past.                |
| `MalformedJwtException` ✅  | JWT is structurally invalid (wrong format, corrupted string).    |
| `SignatureException` ✅     | JWT signature is invalid — could be wrong key or tampered token. |
| `UnsupportedJwtException`  | JWT uses an unsupported format or algorithm.                     |
| `PrematureJwtException`    | JWT has a `nbf` (not before) claim in the future.                |
| `IllegalArgumentException` | Typically when `parseClaimsJws` receives `null` or empty string. |

#### February 21, 2025
**TODO**
- [x] Fix auth service + controller


#### February 24, 2025

Registering
AuthController -> AuthService -> UserClient -> UserService publishes UserCreatedEvent
UserEventListener -> tokenService and mailSender

Reverification
AuthController -> AuthService -> UserClient -> UserService publishes VerificationTokenRequested
UserEventListener -> tokenService and mailSender