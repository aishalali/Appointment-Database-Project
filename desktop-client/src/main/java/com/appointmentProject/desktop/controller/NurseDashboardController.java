/*************************************************************************
 *  NurseDashboardController.java
 *
 *      This Class acts as the caller to the backend for specific data.
 *      It will first connect each table column to a respective field in
 *      a specified table. Using API client, it will talk to Spring Boot
 *      backend. It will receive a response (in JSON format) and turn it
 *      into a Java object. Afterwards it will display the obejcts into
 *      a table for display.
 *
 * @author Matthew Kiyono
 * @version 1.0
 * @since 12/3/2025
 **************************************************************************/
package com.appointmentProject.desktop.controller;
/********************************************************************
 *  NurseDashboardController.java
 *
 *          This controller provides a homepage for the Nurse user
 *          type.
 *
 * @author Matthew Kiyono
 * @version 1.1
 * @since 12/3/2025
 ********************************************************************/
import com.appointmentProject.desktop.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class NurseDashboardController {

    @FXML
    private Button backToLoginButton;

    @FXML
    private void initialize() {
        // Runs automatically when FXML loads
        System.out.println("Nurse Dashboard loaded.");
    }

    @FXML
    public void handleManageAccount() {
        AccountManagementController.setPreviousPage("/fxml/nurse_dashboard.fxml");
        SceneNavigator.switchTo("/fxml/account_management.fxml");
    }

    @FXML
    public void handleViewStaff() {
        ViewStaffController.previousPage = "/fxml/nurse_dashboard.fxml";
        SceneNavigator.switchTo("/fxml/view_staff.fxml");
    }

    @FXML
    private void handleManagePatients(ActionEvent event) {
        ManagePatientController.previousPage = "/fxml/nurse_dashboard.fxml";
        SceneNavigator.switchTo("/fxml/manage_patient.fxml");
    }

    @FXML
    private void handleLogout() {
        SceneNavigator.switchTo("/fxml/login.fxml");
    }
}
