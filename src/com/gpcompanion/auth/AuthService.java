package com.gpcompanion.auth;

import com.gpcompanion.exceptions.*;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
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
        // SecureRandom — pulls from an OS entropy source
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        StringBuilder builder = new StringBuilder();
        for (byte b : salt) {
            // converts raw bytes into a hex string so it can be stored safely
            builder.append(String.format("%02x", b));
        }

        return builder.toString();
    }

    // password taken as char[] instead of String so the caller can wipe it after use —
    // Strings are interned/immutable and can't be zeroed out of memory on demand.
    private String hashPassword(char[] password, String salt) throws NoSuchAlgorithmException {
        char[] saltedChars = new char[salt.length() + password.length];
        salt.getChars(0, salt.length(), saltedChars, 0);
        System.arraycopy(password, 0, saltedChars, salt.length(), password.length);

        ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(CharBuffer.wrap(saltedChars));
        byte[] saltedBytes = Arrays.copyOf(byteBuffer.array(), byteBuffer.limit());

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(saltedBytes);

            StringBuilder hashBuilder = new StringBuilder();
            for (byte b : hash) {
                hashBuilder.append(String.format("%02x", b));
            }

            return hashBuilder.toString();
        } finally {
            Arrays.fill(saltedChars, '\0');
            Arrays.fill(saltedBytes, (byte) 0);
        }
    }

    private void validateCredentials(String username, char[] password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username and password cannot be null");
        }

        if (username.isEmpty() || password.length == 0) {
            throw new IllegalArgumentException("Username and password cannot be empty");
        }

        // was `>= 16`, which rejected exactly-16-char input despite the message saying "exceed"
        if (username.length() > 16 || password.length > 16) {
            throw new IllegalArgumentException("Username and password cannot exceed 16 characters");
        }

        if (username.contains(" ") || username.contains("\n")) {
            throw new IllegalArgumentException("Username cannot contain spaces / new lines");
        }
    }

    public void register(String username, char[] password) throws DuplicateUserException, NoSuchAlgorithmException {
        validateCredentials(username, password);

        if (this.credentials.exists(username)) {
            throw new DuplicateUserException("Error! " + username + " already exists");
        }

        String salt = generateSalt(); // salt generated once per user, per registration
        UserAccount account = new UserAccount(username, hashPassword(password, salt), salt);
        credentials.saveUserAccount(account);
    }

    public void login(String username, char[] password) throws AuthenticationException, NoSuchAlgorithmException {
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