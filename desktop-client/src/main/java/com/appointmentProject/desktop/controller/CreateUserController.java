/********************************************************************
 *  CreateUserController.java
 *
 *          This controller provides a "form" for the admin user to
 *          fill out and create a new user for.
 *
 * @author Matthew Kiyono
 * @version 1.0
 * @since 12/6/2025
 ********************************************************************/

package com.appointmentProject.desktop.controller;

import com.appointmentProject.desktop.SceneNavigator;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreateUserController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField emailField;
    @FXML private ComboBox<String> roleBox;
    @FXML private Label messageLabel;

    @FXML
    private void initialize() {
        roleBox.getItems().addAll("ADMIN", "PROVIDER", "NURSE", "RECEPTIONIST");
    }

    @FXML
    public void handleCreateUser() {

        try {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String email = emailField.getText().trim();
            String role = roleBox.getValue();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || role == null) {
                messageLabel.setText("All fields are required.");
                return;
            }

            // Build JSON request
            JsonObject obj = new JsonObject();
            obj.addProperty("username", username);
            obj.addProperty("password", password);
            obj.addProperty("email", email);
            obj.addProperty("userType", role);

            // Call backend
            URL url = new URL("http://localhost:8080/account/add");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            OutputStream os = con.getOutputStream();
            os.write(obj.toString().getBytes());
            os.flush();
            os.close();

            int status = con.getResponseCode();

            if (status == 200) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("User created successfully!");
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Error: Could not create user.");
            }

        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Server error.");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBack() {
        SceneNavigator.switchTo("/fxml/manage_users.fxml");
    }
}
