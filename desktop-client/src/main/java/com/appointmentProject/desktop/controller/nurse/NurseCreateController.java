/********************************************************************
 *  NurseCreateController.java
 *
 *          This controller is responsible for providing an admin
 *          user the ability to create a new nurse.
 *
 *
 * @author Matthew Kiyono
 * @version 1.0
 * @since 12/7/2025
 ********************************************************************/

package com.appointmentProject.desktop.controller.nurse;

import com.appointmentProject.desktop.SceneNavigator;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NurseCreateController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;

    @FXML private Label messageLabel;

    @FXML
    private void handleCreateNurse() {
        try {
            JsonObject body = new JsonObject();
            body.addProperty("firstName", firstNameField.getText());
            body.addProperty("lastName", lastNameField.getText());
            body.addProperty("phone", phoneField.getText());
            body.addProperty("email", emailField.getText());
            body.addProperty("address", addressField.getText());

            URL url = new URL("http://localhost:8080/nurse/add");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");

            OutputStream os = con.getOutputStream();
            os.write(body.toString().getBytes());
            os.flush();
            os.close();

            if (con.getResponseCode() == 200) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Nurse created successfully!");
            } else {
                messageLabel.setText("Creation failed.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Server error.");
        }
    }

    @FXML
    private void handleBack() {
        SceneNavigator.switchTo("/fxml/nurse_list.fxml");
    }
}
