package com.appointmentProject.desktop.model;

public class User {

    private String username;
    private String email;
    private String userType;

    public User(String username, String email, String userType) {
        this.username = username;
        this.email = email;
        this.userType = userType;
    }

    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getUserType() { return userType; }

    public void setEmail(String email) { this.email = email; }
    public void setUserType(String userType) { this.userType = userType; }
}
