
/*************************************************************************
 *  ProviderDashboardController.java
 *
 *      This controller is responsible for providing providers a home page.
 *
 * @author Matthew Kiyono
 * @version 1.1
 * @since 12/3/2025
 **************************************************************************/
package com.appointmentProject.desktop.controller;

import com.appointmentProject.desktop.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ProviderDashboardController {

    @FXML
    private void initialize() {
        System.out.println("Provider Dashboard Loaded.");
    }

    @FXML
    public void handleManageAccount() {
        AccountManagementController.setPreviousPage("/fxml/provider_dashboard.fxml");
        SceneNavigator.switchTo("/fxml/account_management.fxml");
    }

    @FXML
    public void handleViewStaff() {
        ViewStaffController.previousPage = "/fxml/provider_dashboard.fxml";
        SceneNavigator.switchTo("/fxml/view_staff.fxml");
    }

    @FXML
    private void handleManagePatients(ActionEvent event) {
        ManagePatientController.previousPage = "/fxml/provider_dashboard.fxml";
        SceneNavigator.switchTo("/fxml/manage_patient.fxml");
    }


    @FXML
    private void handleLogout() {
        SceneNavigator.switchTo("/fxml/login.fxml");
    }
}
