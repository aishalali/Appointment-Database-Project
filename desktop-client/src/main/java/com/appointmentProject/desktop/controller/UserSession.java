package com.appointmentProject.desktop.controller;

public class UserSession {

    private static String userType = "ADMIN";  // Temporarily default to ADMIN

    public static void setUserType(String type) {
        userType = type;
    }

    public static boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(userType);
    }
}
