package com.gpcompanion.auth;

/// session context contains data and methods pertaining to the current logged user

public class SessionContext {
    private UserAccount currentUser;

    public void setCurrentUser(UserAccount currentUser) {
        this.currentUser = currentUser;
    }

    public UserAccount getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return this.currentUser != null;
    }

    public void clear() {
        this.currentUser = null;
    }
}
