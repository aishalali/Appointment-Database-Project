
/*************************************************************************
 *  ProviderDashboardController.java
 *
 *      This controller is responsible for providing providers a home page.
 *
 * @author Matthew Kiyono
 * @version 1.0
 * @since 12/3/2025
 **************************************************************************/
package com.appointmentProject.desktop.controller;

import com.appointmentProject.desktop.SceneNavigator;
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
    private void handleLogout() {
        SceneNavigator.switchTo("/fxml/login.fxml");
    }
}
