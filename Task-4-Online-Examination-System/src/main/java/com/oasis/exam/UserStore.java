package com.oasis.exam;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Properties;

public final class UserStore {
    private static final Path FILE = Paths.get("data", "user.properties");
    private UserStore() {}

    public static AppUser loadDemoUser() {
        try {
            if (Files.exists(FILE)) {
                Properties p = new Properties();
                try (Reader r = Files.newBufferedReader(FILE, StandardCharsets.UTF_8)) { p.load(r); }
                return new AppUser(p.getProperty("username", "student"),
                        p.getProperty("displayName", "Oasis Student"),
                        p.getProperty("password", "oasis123"));
            }
        } catch (IOException ex) { ex.printStackTrace(); }
        return new AppUser("student", "Oasis Student", "oasis123");
    }

    public static void save(AppUser user) throws IOException {
        Files.createDirectories(FILE.getParent());
        Properties p = new Properties();
        p.setProperty("username", user.getUsername());
        p.setProperty("displayName", user.getDisplayName());
        p.setProperty("password", user.getPassword());
        try (Writer w = Files.newBufferedWriter(FILE, StandardCharsets.UTF_8)) {
            p.store(w, "OASIS INFOBYTE - Online Examination System user profile");
        }
    }
}
