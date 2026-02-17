package com.notif.api.auth.controller;

import com.notif.api.auth.response.AuthenticatedUserDTO;
import com.notif.api.auth.request.AuthenticationRequest;
import com.notif.api.auth.response.AuthenticationResponse;
import com.notif.api.auth.request.RegisterRequest;
import com.notif.api.auth.service.AuthenticationService;
import com.notif.api.common.response.ApiResponse;
import com.notif.api.common.response.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Value("${api.prefix}")
    private String apiPrefix;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        String response =  "This endpoint is a not secure";
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getCurrentlyLoggedUserInfo(
            HttpServletRequest request,
            HttpServletResponse response,
            @AuthenticationPrincipal UserDetails loggedInUser
    ) throws IOException {
        AuthenticatedUserDTO userInfo = authenticationService.getCurrentlyLoggedUser(request, response, loggedInUser);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Success", userInfo));
    }

    // TODO: Properly structure response so user can get a hint to verify email.
    //  appUrl should be frontend url and not backend.
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(
            @RequestBody @Valid RegisterRequest request,
            HttpServletRequest servletRequest
    ) {
        String appUrl = getAppUrl(servletRequest);

        UserDTO userDTO = authenticationService.register(request, appUrl);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Pending verification", userDTO));
    }

    private String getAppUrl(HttpServletRequest request) {
        String scheme = request.getScheme();                    // http or https
        String serverName = request.getServerName();            // localhost or domain.com
        int serverPort = request.getServerPort();               // 8080
        String contextPath = request.getContextPath();          // usually empty unless deployed with context

        String portPart = (!(serverPort == 80 || serverPort == 443)) ? ":" + serverPort : "";

        return scheme + "://" + serverName + portPart + contextPath + apiPrefix;
    }

    @GetMapping("/confirm-registration")
    public ResponseEntity<ApiResponse> confirmRegistration(
            @RequestParam("token") String token,
            @RequestParam("email") String email
    ) {
        // TODO: Check if token is already used
        UserDTO userDTO = authenticationService.confirmRegistration(token, email);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Email verified successfully.", userDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authenticationService.authenticate(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}