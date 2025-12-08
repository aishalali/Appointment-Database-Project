/********************************************************************
 *  PatientDetailsController.java
 *
 *          This controller is responsible for displaying a specific
 *          Patient's profile: including every field within the
 *          database. Within this page, there is an "update" button
 *          allowing the user to edit the Patient's details.
 *
 * @author Matthew Kiyono
 * @version 1.1
 * @since 12/7/2025
 ********************************************************************/
package com.appointmentProject.desktop.controller;

import com.appointmentProject.desktop.SceneNavigator;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PatientDetailsController {

    @FXML private Label idLabel;
    @FXML private Label firstNameLabel;
    @FXML private Label lastNameLabel;
    @FXML private Label dobLabel;
    @FXML private Label ageLabel;
    @FXML private Label phoneLabel;
    @FXML private Label emailLabel;
    @FXML private Label genderLabel;
    @FXML private Label heightLabel;
    @FXML private Label weightLabel;
    @FXML private Label insuranceLabel;
    @FXML private Label emergencyContactLabel;
    @FXML private Label messageLabel;

    @FXML private Button updateButton;
    @FXML private Button deleteButton;

    @FXML
    private void initialize() {
        loadPatientDetails();

        if (!ManagePatientController.previousPage.equals("/fxml/admin_dashboard.fxml")) {
            deleteButton.setVisible(false);
            deleteButton.setManaged(false);
        }
    }

    private void loadPatientDetails() {
        try {
            int id = ManagePatientController.selectedPatientId;

            URL url = new URL("http://localhost:8080/patient/" + id);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) sb.append(line);
            in.close();

            JsonObject obj = JsonParser.parseString(sb.toString()).getAsJsonObject();

            idLabel.setText(String.valueOf(obj.get("id").getAsInt()));
            firstNameLabel.setText(obj.get("firstName").getAsString());
            lastNameLabel.setText(obj.get("lastName").getAsString());
            dobLabel.setText(obj.get("doB").getAsString());
            ageLabel.setText(String.valueOf(obj.get("age").getAsInt()));
            phoneLabel.setText(obj.get("phone").getAsString());
            emailLabel.setText(obj.get("email").isJsonNull() ? "N/A" : obj.get("email").getAsString());
            genderLabel.setText(obj.get("gender").isJsonNull() ? "N/A" : obj.get("gender").getAsString());
            heightLabel.setText(String.valueOf(obj.get("height").getAsDouble()));
            weightLabel.setText(String.valueOf(obj.get("weight").getAsDouble()));

            insuranceLabel.setText(obj.get("insuranceId").isJsonNull()
                    ? "None" : obj.get("insuranceId").getAsString());
            emergencyContactLabel.setText(obj.get("emergencyContactId").isJsonNull()
                    ? "None" : obj.get("emergencyContactId").getAsString());

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error loading patient details.");
        }
    }

    @FXML
    public void handleBack() {
        SceneNavigator.switchTo("/fxml/manage_patient.fxml");
    }

    @FXML
    public void handleUpdate() {
        SceneNavigator.switchTo("/fxml/patient_edit.fxml");
    }

    @FXML
    public void handleDelete() {
        String fullName = firstNameLabel.getText() + " " + lastNameLabel.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Patient");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete Patient " + fullName + "?");

        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        var result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                int id = Integer.parseInt(idLabel.getText());
                URL url = new URL("http://localhost:8080/patient/delete/" + id);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("DELETE");

                int status = con.getResponseCode();
                if (status == 200) {

                    ManagePatientController.deletionMessage =
                            "Patient " + fullName + " was successfully deleted.";

                    SceneNavigator.switchTo("/fxml/manage_patient.fxml");
                } else {
                    messageLabel.setText("Failed to delete patient. (HTTP " + status + ")");
                }

            } catch (Exception e) {
                e.printStackTrace();
                messageLabel.setText("Error deleting patient.");
            }
        }
    }
}
