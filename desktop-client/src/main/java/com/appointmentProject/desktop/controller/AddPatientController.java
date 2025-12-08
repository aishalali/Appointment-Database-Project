/*************************************************************************
 *  AddPatientController.java
 *
 *      This controller is responsible for providing a "form" to an admin
 *      or receptionist user type that will add a Patient to the database.
 *
 * @author Matthew Kiyono
 * @version 1.0
 * @since 12/7/2025
 **************************************************************************/
package com.appointmentProject.desktop.controller;

import com.appointmentProject.desktop.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

public class AddPatientController {

    // Required fields
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private DatePicker dobPicker;
    @FXML private TextField ageField;
    @FXML private TextField weightField;
    @FXML private TextField heightField;
    @FXML private TextField phoneField;

    // Optional fields
    @FXML private TextField genderField;
    @FXML private TextField emailField;
    @FXML private TextField insuranceIdField;
    @FXML private TextField emergencyContactIdField;

    @FXML private Label statusLabel;

    @FXML
    private void handleSubmit(ActionEvent event) {

        // In Option B, we do NOT connect backend yet (we can add that later)
        // This only validates required fields.

        if (firstNameField.getText().isBlank() ||
                lastNameField.getText().isBlank() ||
                dobPicker.getValue() == null ||
                ageField.getText().isBlank() ||
                weightField.getText().isBlank() ||
                heightField.getText().isBlank() ||
                phoneField.getText().isBlank()) {

            statusLabel.setText("⚠ Please fill in all required fields.");
            return;
        }

        statusLabel.setText("✔ Patient validated (backend call will be added later)");
    }

    @FXML
    private void handleBack(ActionEvent event) {
        SceneNavigator.switchTo("/fxml/manage_patient.fxml");
    }
}
