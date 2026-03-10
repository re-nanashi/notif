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