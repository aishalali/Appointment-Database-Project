/********************************************************************
 *  LoginController.java
 *
 *          This class provides a working login screen and screen
 *          navigator. Depending on the user type will determine
 *          which dashboard they are sent to.
 *
 * @author Matthew Kiyono
 * @version 1.0
 * @since 12/1/2025
 ********************************************************************/

package com.appointmentProject.desktop.controller;

import com.appointmentProject.desktop.SceneNavigator;
import com.appointmentProject.desktop.SessionData;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        System.out.println("[LOGIN] Username: " + username);
        System.out.println("[LOGIN] Password: " + password);

        try {
            String urlString = "http://localhost:8080/api/auth/login?username="
                    + username + "&password=" + password;

            System.out.println("[LOGIN] Request URL: " + urlString);

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int status = conn.getResponseCode();
            System.out.println("[LOGIN] HTTP status: " + status);

            // Choose input stream based on status (prevents silent failures)
            InputStream is = (status >= 200 && status < 300)
                    ? conn.getInputStream()
                    : conn.getErrorStream();

            if (is == null) {
                errorLabel.setText("Login error: empty response from server.");
                System.out.println("[LOGIN] No response body from server.");
                return;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String responseLine = in.readLine();
            in.close();

            if (responseLine == null) {
                errorLabel.setText("Login error: empty response.");
                System.out.println("[LOGIN] Response line is null.");
                return;
            }

            String response = responseLine.trim();
            System.out.println("[LOGIN] Backend response: '" + response + "'");
            System.out.println("[LOGIN RAW BYTES] " + Arrays.toString(response.getBytes()));

            SessionData.currentUsername = username;
            SessionData.currentUserRole = response;


            switch (response) {
                case "ADMIN":
                    SceneNavigator.switchTo("/fxml/admin_dashboard.fxml");
                    return;

                case "PROVIDER":
                    SceneNavigator.switchTo("/fxml/provider_dashboard.fxml");
                    return;

                case "NURSE":
                    SceneNavigator.switchTo("/fxml/nurse_dashboard.fxml");
                    return;

                case "RECEPTIONIST":
                    SceneNavigator.switchTo("/fxml/receptionist_dashboard.fxml");
                    return;

                case "INVALID":
                    errorLabel.setText("Invalid username or password.");
                    return;

                default:
                    // Anything unexpected gets shown clearly
                    errorLabel.setText("Login error: unexpected response '" + response + "'");
                    System.out.println("[LOGIN] Unexpected response from backend: " + response);
                    return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Login error: " + e.getClass().getSimpleName());
        }
    }
}
