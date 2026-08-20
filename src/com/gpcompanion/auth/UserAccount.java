package com.gpcompanion.auth;

import java.util.Objects;

/// user account contains immutable fields that cannot be changed after creation
/// data is encapsulated and does not have any mutator methods, can only be accessed through getters

public class UserAccount {
    // final fields, set once in the constructor, never reassigned.
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

    // username is the natural identity key — same reasoning as Driver in the race package
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount)) return false;
        UserAccount other = (UserAccount) o;
        return username.equals(other.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}