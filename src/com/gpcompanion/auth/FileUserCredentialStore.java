package com.gpcompanion.auth;

import java.util.*;
import java.io.*;

/// implements UserCredentialStore

public class FileUserCredentialStore implements UserCredentialStore{
    private final String filePath;
    private final Map<String, UserAccount> cache;

    public FileUserCredentialStore(String filePath, Map<String, UserAccount> cache) {
        this.filePath = filePath;
        this.cache = cache;
        loadAll();
    }

    public void loadAll() {

    }



    @Override
    public boolean exists(String username) {
        return false;
    }

    @Override
    public Optional<UserAccount> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public void saveUserAccount(UserAccount account) {

    }
}
