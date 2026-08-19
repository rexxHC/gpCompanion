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
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("Error! File does not exist");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                UserAccount account = new UserAccount(tokens[0], tokens[1], tokens[2]);
                cache.put(account.getUsername(), account);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Credentials / File corrupted", e);
        }
    }

    public void saveAll() {
        File file = new File(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (UserAccount account : cache.values()) {
                String line = String.join(",", account.getUsername(), account.getPasswordHash(), account.getSalt());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save Credentials / File corrupted", e);
        }
    }

    @Override
    public boolean exists(String username) {
        return cache.containsKey(username);
    }

    @Override
    public Optional<UserAccount> findByUsername(String username) {
        return Optional.ofNullable(cache.get(username));
    }

    @Override
    public void saveUserAccount(UserAccount account) {
        cache.put(account.getUsername(), account);
        saveAll();
    }
}
