package com.oasis.exam;

public class AppUser {
    private String username;
    private String displayName;
    private String password;

    public AppUser(String username, String displayName, String password) {
        this.username = username;
        this.displayName = displayName;
        this.password = password;
    }
    public String getUsername() { return username; }
    public String getDisplayName() { return displayName; }
    public String getPassword() { return password; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public void setPassword(String password) { this.password = password; }
}
