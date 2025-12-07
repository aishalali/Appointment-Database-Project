package com.appointmentProject.desktop.controller;

import com.appointmentProject.desktop.SceneNavigator;
import javafx.fxml.FXML;

public class ManageNursesController {

    @FXML
    public void handleBack() {
        SceneNavigator.switchTo("/fxml/manage_staff.fxml");
    }
}
