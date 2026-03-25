package com.notif.api.auth.application.service;

import com.notif.api.auth.api.dto.*;
import com.notif.api.auth.application.dto.*;
import com.notif.api.auth.domain.exception.SessionRevokedException;
import com.notif.api.auth.domain.exception.TokenExpiredException;
import com.notif.api.auth.domain.exception.TokenRevokedException;
import com.notif.api.auth.domain.model.SessionRevokedReason;
import com.notif.api.auth.infrastructure.security.JwtTokenProvider;
import com.notif.api.auth.infrastructure.security.NotifUserDetails;
import com.notif.api.core.constants.AppConstants;
import com.notif.api.core.exception.ErrorCode;
import com.notif.api.user.api.dto.UserAuthDetails;
import com.notif.api.user.api.dto.UserResponse;
import com.notif.api.user.api.dto.CreateUserRequest;
import com.notif.api.user.api.client.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * Implementation of AuthenticationService that handles login, registration,
 * email verification, and token issuance. It authenticates users via Spring Security,
 * generates JWT access tokens, manages refresh tokens, and delegates user-related
 * operations to the User service through UserClient.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserClient userClient;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final DeviceService deviceService;
    private final UserDeviceService userDeviceService;
    private final SessionService sessionService;
    private final SessionRevocationService sessionRevocationService;

    /**
     * Registers a new user by delegating user creation to the User service.
     * Returns registration details and prompts email verification.
     */
    @Override
    public RegisterResponse register(RegisterRequest request) {
        // TODO: Add auth-specific fields to RegisterRequest (e.g., agreeToTerms, captchaToken) then validate
        // Map RegisterRequest to CreateUserRequest
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .confirmPassword(request.getConfirmPassword())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();

        UserResponse createdUser = userClient.createUser(createUserRequest);

        return mapUserToRegisterResponse(
                createdUser,
                "Action Required: Please verify your email to activate your account."
        );
    }

    /**
     * Confirms user registration by validating the verification token
     * and enabling the user account.
     */
    @Override
    public RegisterResponse confirmRegistration(String token, String userEmail) {
        UserResponse activatedUser = userClient.enableUser(token, userEmail);

        return mapUserToRegisterResponse(
                activatedUser,
                "Email verified successfully. You can now log in to your account."
        );
    }

    /**
     * Triggers re-sending of the email verification link
     * for users who have not yet verified their account.
     */
    @Override
    public RegisterResponse resendVerificationEmail(String userEmail) {
        UserResponse requestedUser = userClient.requestVerification(userEmail);

        return mapUserToRegisterResponse(
                requestedUser,
                "Action Required: Please verify your email to activate your account."
        );
    }

    /**
     * Helper function for mapping user information into response.
     */
    private RegisterResponse mapUserToRegisterResponse(UserResponse user, String message) {
        return RegisterResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .emailVerified(user.isEmailVerified())
                .fullName(user.getFullName())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .message(message)
                .build();
    }

    /**
     * Retrieves the currently authenticated user's profile information
     * from the User service using their email identifier.
     */
    @Override
    public AuthenticatedUserResponse getAuthenticatedUser(UUID id) {
        UserResponse user = userClient.getUserById(id);

        return AuthenticatedUserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .fullName(user.getFullName())
                .role(user.getRole())
                .build();
    }

    /**
     * Authenticates user credentials, generates a JWT access token and
     * refresh token, and returns both as part of the authentication result.
     */
    @Override
    @Transactional
    public AuthenticationResult<LoginResponse> authenticate(
            LoginRequest request,
            AuthenticationRequestContext context
    ) {
        // Delegate credential validation to Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Extract authenticated user information then load full user profile
        NotifUserDetails userDetails = (NotifUserDetails)authentication.getPrincipal();
        AuthenticatedUserResponse userInfo = getAuthenticatedUser(userDetails.getId());

        // Register a new device or return an existing device from the DB
        DeviceDto device = deviceService.registerDevice(context.getDeviceId(), context.getUserAgent());
        // Link user and device or return an existing user device entry from DB
        // TODO (future): add MFA if device not trusted
        UserDeviceDto userDevice = userDeviceService.registerLogin(userInfo, device);

        // Revoke the current active session on the device (if any) before creating a new one
        revokeExistingDeviceSession(device.getId());
        // Create new session
        SessionDto session = sessionService.createSession(
                userInfo.getId(),
                device.getId(),
                context.getClientIp()
        );

        // Issue long-lived refresh token
        RefreshTokenDto refreshToken = refreshTokenService.generateToken(session.getId());

        // Issue short-lived JWT access token
        String jwtToken = jwtTokenProvider.generateToken(userDetails);
        Date expiration = jwtTokenProvider.extractExpiration(jwtToken);
        long expiresIn = (expiration.getTime() - System.currentTimeMillis()) / AppConstants.MILLISECONDS_PER_SECOND;

        // Map login response
        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken(jwtToken)
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .user(userInfo)
                .build();

        // Map cookie payload
        CookiePayload cookies = CookiePayload.builder()
                .refreshToken(refreshToken.getToken())
                .deviceId(device.getDeviceId().toString())
                .build();

        return new AuthenticationResult<>(loginResponse, cookies);
    }

    private void revokeExistingDeviceSession(UUID deviceId) {
        sessionService.getActiveSessionOnDevice(deviceId)
                .map(SessionDto::getId)
                .ifPresent(id ->
                        sessionRevocationService.revokeSession(id, SessionRevokedReason.SESSION_REPLACED)
                );
    }

    @Override
    @Transactional
    public AuthenticationResult<LoginResponse> refresh(
            String refreshTokenString,
            AuthenticationRequestContext context
    ) {
        // Fetch active session from token; will throw an exception if session is not valid
        SessionDto currentSession = getValidatedSession(refreshTokenString, context.getDeviceId());

        // Rotate refresh token; consumes previous token and generates a new one
        RefreshTokenDto newRefreshToken = refreshTokenService.rotate(refreshTokenString);

        // Extract associated user from the current session
        UserAuthDetails associatedUser = userClient.getUserAuthDetailsById(currentSession.getUserId());
        NotifUserDetails userDetails = new NotifUserDetails(associatedUser);

        // Generate a new short-lived JWT access token via JwtService
        String jwtToken = jwtTokenProvider.generateToken(userDetails);
        Date expiration = jwtTokenProvider.extractExpiration(jwtToken);
        long expiresIn = (expiration.getTime() - System.currentTimeMillis()) / AppConstants.MILLISECONDS_PER_SECOND;

        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken(jwtToken)
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .build();

        CookiePayload cookies = CookiePayload.builder()
                .refreshToken(newRefreshToken.getToken())
                .deviceId(null)
                .build();

        return new AuthenticationResult<>(loginResponse, cookies);
    }

    @Override
    @Transactional
    public AuthenticationResult<LogoutResponse> logout(
            String refreshTokenString,
            AuthenticationRequestContext context
    ) {
        if (refreshTokenString != null) {
            // Fetch session details from refresh token cookie retrieved device
            SessionDto validatedSession = getValidatedSession(refreshTokenString, context.getDeviceId());
            sessionRevocationService.revokeSession(validatedSession.getId(), SessionRevokedReason.LOGOUT);
        }

        return new AuthenticationResult<>(
                new LogoutResponse("Logged out successfully", Instant.now()),
                null
        );
    }

    @Override
    @Transactional
    public AuthenticationResult<LogoutResponse> logoutAllDevices(
            String refreshTokenString,
            AuthenticationRequestContext context
    ) {
        if (refreshTokenString != null) {
            // Fetch session details from refresh token cookie retrieved device
            SessionDto validatedSession = getValidatedSession(refreshTokenString, context.getDeviceId());
            sessionRevocationService.revokeAllUserSessions(validatedSession.getUserId(), SessionRevokedReason.LOGOUT);
        }

        return new AuthenticationResult<>(
                new LogoutResponse("Logged out out of all devices successfully", Instant.now()),
                null
        );
    }

    @Transactional(
            propagation = Propagation.REQUIRES_NEW,
            noRollbackFor = {
                    TokenRevokedException.class,
                    TokenExpiredException.class,
                    SessionRevokedException.class
            }
    )
    private SessionDto getValidatedSession(String refreshTokenString, String cookieDeviceId) {
        // Fetch refresh token details from DB
        RefreshTokenDto refreshToken = refreshTokenService.getToken(refreshTokenString);
        // Validate token; revoke sessions if token is misused
        try {
            refreshTokenService.validateToken(refreshToken);
        } catch (TokenRevokedException ex) {
            // If old/used token, revoke all user sessions and all the tokens related to those sessions
            sessionRevocationService.revokeAllUserSessions(refreshToken.getUserId(), SessionRevokedReason.TOKEN_REUSE);
            throw ex;
        } catch (TokenExpiredException ex) {
            // If expired, revoke session and update status to 'EXPIRED' due to inactivity
            sessionRevocationService.revokeSession(refreshToken.getSessionId(), SessionRevokedReason.IDLE_TIMEOUT);
            throw ex;
        }

        // Fetch active session from token; will throw an exception if session is not valid
        SessionDto currentSession = sessionService.getActiveSession(refreshToken.getSessionId());

        // Reject if the session's refresh token is being used from an unrecognized or mismatched device
        // TODO (future): Suspicious login/refresh request (IP/User-Agent/Geo)
        boolean isDeviceMismatch = deviceService.getDevice(cookieDeviceId)
                .map(DeviceDto::getId)
                .filter(id -> id.equals(currentSession.getDeviceId()))
                .isEmpty();
        if (isDeviceMismatch) {
            // Revoke the session and block the request
            sessionRevocationService.revokeSession(currentSession.getId(), SessionRevokedReason.DEVICE_MISMATCH);

            throw new SessionRevokedException(
                    "This session is invalid on the current device. Please log in again.",
                    ErrorCode.AUTH_SESSION_INVALID
            );
        }

        return currentSession;
    }
}