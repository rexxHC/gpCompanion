package com.gpcompanion.auth;

import java.util.*;
import java.io.*;

/// implements UserCredentialStore

public class FileUserCredentialStore implements UserCredentialStore {
    private final String filePath;
    private final Map<String, UserAccount> cache; // cache holds every user in memory

    public FileUserCredentialStore(String filePath, Map<String, UserAccount> cache) {
        this.filePath = filePath;
        this.cache = cache;
        loadAll(); // loads the cache from users.txt
    }

    public void loadAll() {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("Error! File does not exist");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // try block automatically closes the reader even if there is an exception
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) continue;

                String[] tokens = line.split(",");
                // each line formatted into -> [username, passwordHash, salt]
                if (tokens.length != 3) {
                    // skip corrupted rows instead of crashing the whole app on startup
                    System.err.println("Skipping malformed credential entry on line " + lineNumber);
                    continue;
                }

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
                // rewrites the file every time
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
        // O(1) lookup time since a map is used
        return cache.containsKey(username);
    }

    @Override
    public Optional<UserAccount> findByUsername(String username) {
        // Optional wraps the possible null value and forces an explicit handling of the error
        return Optional.ofNullable(cache.get(username));
    }

    @Override
    public void saveUserAccount(UserAccount account) {
        // updates the memory then writes to the disk
        cache.put(account.getUsername(), account);
        saveAll();
    }
}