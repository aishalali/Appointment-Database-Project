/********************************************************************
 *  AccountManagementController.java
 *
 *          This controller is responsible for providing the option
 *          to change the user's password or email address.
 *
 * @author Matthew Kiyono
 * @version 1.0
 * @since 12/6/2025
 ********************************************************************/

package com.appointmentProject.desktop.controller;

import com.appointmentProject.desktop.SceneNavigator;
import javafx.fxml.FXML;

public class AccountManagementController {

    private static String previousPage = "/fxml/login.fxml";

    public static void setPreviousPage(String fxmlPath) {
        previousPage = fxmlPath;
    }

    @FXML
    public void handleBack() {
        SceneNavigator.switchTo(previousPage);
    }

    @FXML
    public void handleEditEmail() {
        SceneNavigator.switchTo("/fxml/edit_email.fxml");
    }

    @FXML
    public void handleChangePassword() {
        SceneNavigator.switchTo("/fxml/edit_password.fxml");
    }


}
