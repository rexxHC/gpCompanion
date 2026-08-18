package com.gpcompanion.auth;

/// user account contains immutable fields that cannot be changed after creation
/// data is encapsulated and does not have any mutator methods, can only be accessed through getters

public class UserAccount {
    private final String username;
    private final String passwordHash;
    private final String salt;

    public UserAccount(String username, String passwordHash, String salt) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.salt = salt;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getSalt() {
        return salt;
    }
}
