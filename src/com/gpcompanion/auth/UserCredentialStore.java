package com.gpcompanion.auth;

import java.util.*;

/// interface for obscuring credential storing through abstraction

public interface UserCredentialStore {
    boolean exists(String username);
    Optional<UserAccount> findByUsername(String username);
    void saveUserAccount(UserAccount account);
}
