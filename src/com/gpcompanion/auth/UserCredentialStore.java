package com.gpcompanion.auth;

import java.util.*;

/// interface for obscuring credential storing through abstraction
/// defines what the credential store does

public interface UserCredentialStore {
    boolean exists(String username);
    Optional<UserAccount> findByUsername(String username);
    void saveUserAccount(UserAccount account);
}