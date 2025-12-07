package com.appointmentProject.desktop.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserRow {

    private final StringProperty username;
    private final StringProperty email;
    private final StringProperty role;

    public UserRow(String username, String email, String role) {
        this.username = new SimpleStringProperty(username);
        this.email = new SimpleStringProperty(email);
        this.role = new SimpleStringProperty(role);
    }

    public StringProperty usernameProperty() { return username; }
    public StringProperty emailProperty() { return email; }
    public StringProperty roleProperty() { return role; }

    public String getUsername() { return username.get(); }
    public String getEmail() { return email.get(); }
    public String getRole() { return role.get(); }

}
