/********************************************************************
 *  ManageStaffController.java
 *
 *          This controller provides a menu for admin users to
 *
 * @author Matthew Kiyono
 * @version 1.0
 * @since 12/6/2025
 ********************************************************************/
package com.appointmentProject.desktop.controller;

import com.appointmentProject.desktop.SceneNavigator;
import javafx.fxml.FXML;

public class ManageStaffController {

    @FXML
    public void handleProviders() {
        SceneNavigator.switchTo("/fxml/provider_list.fxml");
    }

    @FXML
    public void handleNurses() {
        SceneNavigator.switchTo("/fxml/nurse_list.fxml");
    }

    @FXML
    public void handleBack() {
        SceneNavigator.switchTo("/fxml/admin_dashboard.fxml");
    }
}
