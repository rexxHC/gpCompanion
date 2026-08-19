package com.gpcompanion.auth;

import com.gpcompanion.exceptions.*;

import java.security.*;
import java.util.Optional;

/// auth service contains all the necessary login validation logic and hashing algorithms

public class AuthService {
    private final UserCredentialStore credentials;
    private final SessionContext session;

    public AuthService(UserCredentialStore credentials, SessionContext session) {
        this.credentials = credentials;
        this.session = session;
    }

    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        StringBuilder builder = new StringBuilder();
        for (byte b : salt) {
            builder.append(String.format("%02x", b));
        }

        return builder.toString();
    }

    private String hashPassword(String password, String salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest((salt + password).getBytes());

        StringBuilder hashBuilder = new StringBuilder();
        for (byte b : hash) {
            hashBuilder.append(String.format("%02x", b));
        }

        return hashBuilder.toString();
    }

    private void validateCredentials(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username and password cannot be null");
        }

        if (username.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Username and password cannot be empty");
        }

        if (username.length() != 16 || password.length() != 16) {
            throw new IllegalArgumentException("Username and password cannot exceed 16 characters");
        }

        if(username.contains(" ") || username.contains("\n")) {
            throw new IllegalArgumentException("Username cannot contain spaces / new lines");
        }
    }

    public void register(String username, String password) throws DuplicateUserException, NoSuchAlgorithmException {
        validateCredentials(username, password);

        if (this.credentials.exists(username)) {
            throw new DuplicateUserException("Error! " + username + " already exists");
        }

        String salt = generateSalt();
        UserAccount account = new UserAccount(username, hashPassword(password, salt), salt);
        credentials.saveUserAccount(account);
    }

    public void login(String username, String password) throws AuthenticationException, NoSuchAlgorithmException {
        validateCredentials(username, password);

        Optional<UserAccount> optionalUserAccount = credentials.findByUsername(username);
        UserAccount account;
        if (optionalUserAccount.isPresent()) {
            account = optionalUserAccount.get();
        } else {
            throw new AuthenticationException("Username or password is invalid");
        }

        if (hashPassword(password, account.getSalt()).equals(account.getPasswordHash())) {
            session.setCurrentUser(account);
        } else {
            throw new AuthenticationException("Username or password is invalid");
        }
    }

}
