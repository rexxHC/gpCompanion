package com.gpcompanion.auth;

/// session context contains data and methods pertaining to the current logged user
/// tracks whichever UserAccount is currently logged in

public class SessionContext {

    private UserAccount currentUser;

    public void setCurrentUser(UserAccount currentUser) {
        this.currentUser = currentUser;
    }

    public UserAccount getCurrentUser() {
        return currentUser;
    }

    // true on successful log in
    public boolean isLoggedIn() {
        if (currentUser != null) {
            return true;
        } else {
            return false;
        }
    }

    // clears logged-in user
    public void clear() {
        currentUser = null;
    }
}