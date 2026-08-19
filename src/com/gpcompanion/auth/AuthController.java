package com.gpcompanion.auth;

import com.gpcompanion.exceptions.*;

import java.security.NoSuchAlgorithmException;

/// sits between the GUI and AuthService, GUI only directly interacts with this class

public class AuthController {
    private final AuthService authService;
    private final SessionContext session;

    public AuthController(AuthService authService, SessionContext session) {
        this.authService = authService;
        this.session = session;
    }

    public void handleLogin(String username, String password) throws AuthenticationException, NoSuchAlgorithmException {
        authService.login(username, password);
    }

    public void handleRegister(String username, String password) throws DuplicateUserException, NoSuchAlgorithmException {
        authService.register(username, password);
    }

    public SessionContext getSession() {
        return session;
    }
}