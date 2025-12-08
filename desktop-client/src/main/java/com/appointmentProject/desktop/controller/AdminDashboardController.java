/*************************************************************************
 *  AdminDashboardController.java
 *
 *      This controller acts as the caller to the backend for specific data.
 *      It will first connect each table column to a respective field in
 *      a specified table. Using API client, it will talk to Spring Boot
 *      backend. It will receive a response (in JSON format) and turn it
 *      into a Java object. Afterward it will display the objects into
 *      a table for display.
 *
 * @author Matthew Kiyono
 * @version 1.0
 * @since 12/1/2025
 **************************************************************************/

package com.appointmentProject.desktop.controller;

import com.appointmentProject.desktop.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class AdminDashboardController {

    @FXML
    private void initialize() {
        System.out.println("Admin Dashboard Loaded.");
    }


    @FXML
    public void handleManageAccount() {
        AccountManagementController.setPreviousPage("/fxml/admin_dashboard.fxml");
        SceneNavigator.switchTo("/fxml/account_management.fxml");
    }

    @FXML
    public void handleManageUsers() {
        SceneNavigator.switchTo("/fxml/manage_users.fxml");
    }

    @FXML
    public void handleManageStaff() {
        SceneNavigator.switchTo("/fxml/manage_staff.fxml");
    }

    @FXML
    private void handleManagePatients(ActionEvent event) {
        ManagePatientController.previousPage = "/fxml/admin_dashboard.fxml";
        SceneNavigator.switchTo("/fxml/manage_patient.fxml");
    }

    @FXML
    private void handleLogout() {
        SceneNavigator.switchTo("/fxml/login.fxml");
    }
}