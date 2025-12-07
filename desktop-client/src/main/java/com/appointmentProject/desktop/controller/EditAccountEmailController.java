/********************************************************************
 *  EditAccountEmailController.java
 *
 *          This controller provides a "form" for the user to provide
 *          a new email address that is not the same or already taken
 *          by another user.
 *
 * @author Matthew Kiyono
 * @version 1.0
 * @since 12/6/2025
 ********************************************************************/
package com.appointmentProject.desktop.controller;

import com.appointmentProject.desktop.SceneNavigator;
import com.appointmentProject.desktop.SessionData;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class EditAccountEmailController {

    @FXML
    private TextField emailField;

    @FXML
    private Label messageLabel;

    @FXML
    public void handleBack() {
        SceneNavigator.switchTo("/fxml/account_management.fxml");
    }

    @FXML
    public void handleSaveEmail() {
        try {
            String newEmail = emailField.getText().trim();

            // 1 Field blank
            if (newEmail.isEmpty()) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Email cannot be empty.");
                return;
            }

            // Encode parameters to avoid server 500 errors
            String encodedEmail = URLEncoder.encode(newEmail, StandardCharsets.UTF_8);
            String encodedUsername = URLEncoder.encode(SessionData.currentUsername, StandardCharsets.UTF_8);

            // 2. Check if email already exists
            String checkUrl =
                    "http://localhost:8080/account/check-email?email=" + encodedEmail;

            URL url = new URL(checkUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String result = in.readLine();
            in.close();

            // result = "true" (email exists) or "false" (available)
            if (result.equals("true")) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("This email is already in use.");
                return;
            }

            // 3️ Update email on backend
            String updateUrl =
                    "http://localhost:8080/account/update-email?username=" +
                            encodedUsername +
                            "&newEmail=" +
                            encodedEmail;

            URL url2 = new URL(updateUrl);
            HttpURLConnection con2 = (HttpURLConnection) url2.openConnection();

            // Your backend expects POST, NOT PUT
            con2.setRequestMethod("PUT");

            BufferedReader in2 = new BufferedReader(new InputStreamReader(con2.getInputStream()));
            String updateResult = in2.readLine();
            in2.close();

            // updateResult will be "true" or "false"
            if (updateResult.equals("true")) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Email updated successfully!");
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Could not update email.");
            }

        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Server error.");
            e.printStackTrace();
        }
    }
}
