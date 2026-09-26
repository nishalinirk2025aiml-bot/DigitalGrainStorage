package com.example.DigitalGrainStorage.controller;

import com.example.DigitalGrainStorage.model.User;
import com.example.DigitalGrainStorage.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.web.context.SecurityContextRepository;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    public AuthController(
            AuthService authService,
            AuthenticationManager authenticationManager,
            SecurityContextRepository securityContextRepository) {

        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository =
                securityContextRepository;
    }

    // =========================
    // REGISTER
    // =========================

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request) {

        try {

            User user = authService.registerUser(
                    request.username(),
                    request.password()
            );

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AuthResponse(
                            "Registration successful",
                            user.getUsername(),
                            user.getRole()
                    ));

        } catch (RuntimeException e) {

            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    // =========================
    // LOGIN
    // =========================

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {

        try {

            UsernamePasswordAuthenticationToken token =
                    UsernamePasswordAuthenticationToken
                            .unauthenticated(
                                    request.username(),
                                    request.password()
                            );

            Authentication authentication =
                    authenticationManager.authenticate(token);

            SecurityContext context =
                    SecurityContextHolder.createEmptyContext();

            context.setAuthentication(authentication);

            SecurityContextHolder.setContext(context);

            securityContextRepository.saveContext(
                    context,
                    httpRequest,
                    httpResponse
            );

            return ResponseEntity.ok(
                    new AuthResponse(
                            "Login successful",
                            authentication.getName(),
                            authentication
                                    .getAuthorities()
                                    .iterator()
                                    .next()
                                    .getAuthority()
                    )
            );

        } catch (AuthenticationException e) {

            return ResponseEntity.status(
                    HttpStatus.UNAUTHORIZED
            ).body(
                    new ErrorResponse(
                            "Invalid username or password"
                    )
            );
        }
    }

    // =========================
    // CURRENT USER
    // =========================

    @GetMapping("/me")
    public ResponseEntity<?> currentUser(
            Authentication authentication) {

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            return ResponseEntity.status(
                    HttpStatus.UNAUTHORIZED
            ).body(
                    new ErrorResponse("Not logged in")
            );
        }

        User user =
                authService.getUser(
                        authentication.getName()
                );

        return ResponseEntity.ok(
                new AuthResponse(
                        "Authenticated",
                        user.getUsername(),
                        user.getRole()
                )
        );
    }

    // =========================
    // LOGOUT
    // =========================

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpServletRequest request,
            HttpServletResponse response) {

        request.getSession().invalidate();

        SecurityContextHolder.clearContext();

        return ResponseEntity.ok(
                new MessageResponse(
                        "Logout successful"
                )
        );
    }

    // =========================
    // REQUEST CLASSES
    // =========================

    public record RegisterRequest(
            String username,
            String password
    ) {
    }

    public record LoginRequest(
            String username,
            String password
    ) {
    }

    public record AuthResponse(
            String message,
            String username,
            String role
    ) {
    }

    public record ErrorResponse(
            String message
    ) {
    }

    public record MessageResponse(
            String message
    ) {
    }
}