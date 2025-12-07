/********************************************************************
 *  EditPasswordController.java
 *
 *          This controller provides a "form" for the user to fill
 *          out and create a new password. It cannot be the same as
 *          their previous one.
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
import javafx.scene.control.PasswordField;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EditPasswordController {

    @FXML private PasswordField oldPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label messageLabel;

    @FXML
    public void handleBack() {
        SceneNavigator.switchTo("/fxml/account_management.fxml");
    }

    @FXML
    public void handleSavePassword() {
        try {
            String oldPw = oldPasswordField.getText().trim();
            String newPw = newPasswordField.getText().trim();
            String confirmPw = confirmPasswordField.getText().trim();

            // Validation
            if (oldPw.isEmpty() || newPw.isEmpty() || confirmPw.isEmpty()) {
                messageLabel.setText("All fields are required.");
                return;
            }

            if (!newPw.equals(confirmPw)) {
                messageLabel.setText("New passwords do not match.");
                return;
            }

            String urlStr = "http://localhost:8080/account/update-password?"
                    + "username=" + SessionData.currentUsername
                    + "&oldPassword=" + oldPw
                    + "&newPassword=" + newPw;

            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String result = in.readLine();
            in.close();

            if ("true".equals(result)) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Password updated successfully!");
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Could not update password. Check your current password.");
            }

        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Server error.");
            e.printStackTrace();
        }
    }
}
