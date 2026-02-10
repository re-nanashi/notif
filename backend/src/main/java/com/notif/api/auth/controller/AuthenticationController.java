package com.notif.api.auth.controller;

import com.notif.api.auth.dto.AuthenticatedUserDTO;
import com.notif.api.auth.dto.AuthenticationRequest;
import com.notif.api.auth.dto.AuthenticationResponse;
import com.notif.api.auth.dto.RegisterRequest;
import com.notif.api.auth.service.AuthenticationService;
import com.notif.api.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest request) {
        AuthenticationResponse response = authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authenticationService.authenticate(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}