/****************************************************************************************
 *   EditUserController.java
 *
 *      This controller is responsible for providing the admin user a "form" to fill out
 *      to edit an existing user. Here, they can modify their email address or user type.
 *      They can also delete the user as well.
 *
 * @author Matthew Kiyono
 * @since 12/6/2025
 * @version 1.0
 *****************************************************************************************/
package com.appointmentProject.desktop.controller;

import com.appointmentProject.desktop.SceneNavigator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class EditUserController {

    public static String selectedUsername;   // set by ManageUsersController
    private final Gson gson = new Gson();

    @FXML private TextField emailField;
    @FXML private ComboBox<String> roleBox;
    @FXML private Label messageLabel;

    @FXML
    private void initialize() {
        roleBox.getItems().addAll("ADMIN", "PROVIDER", "NURSE", "RECEPTIONIST");
        loadUserData();
    }


    // LOAD USER DATA
    private void loadUserData() {
        try {
            URL url = new URL("http://localhost:8080/account/" + selectedUsername);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            JsonObject obj = gson.fromJson(in.readLine(), JsonObject.class);
            in.close();

            emailField.setText(obj.get("email").getAsString());
            roleBox.setValue(obj.get("userType").getAsString());

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error loading user.");
        }
    }


    // SAVE CHANGES
    @FXML
    public void handleSaveUser() {
        try {
            String newEmail = emailField.getText().trim();
            String newRole = roleBox.getValue();

            if (newEmail.isEmpty()) {
                messageLabel.setText("Email cannot be empty.");
                return;
            }

            // Correct backend URL (query params, NOT JSON body)
            String urlStr =
                    "http://localhost:8080/account/admin-update?"
                            + "username=" + selectedUsername
                            + "&email=" + newEmail
                            + "&role=" + newRole;

            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");

            int status = con.getResponseCode();

            if (status == 200) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("User updated successfully!");
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Update failed.");
            }

        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Server error.");
            e.printStackTrace();
        }
    }

    // DELETE USER  (admin protected)
    @FXML
    public void handleDeleteUser() {
        try {
            if (selectedUsername.equals("admin")) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("The admin account cannot be deleted.");
                return;
            }

            URL url = new URL("http://localhost:8080/account/delete/" + selectedUsername);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");

            int status = con.getResponseCode();

            if (status == 200) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("User deleted successfully!");
            } else if (status == 403) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Cannot delete admin.");
            } else if (status == 404) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("User not found.");
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Server error deleting user.");
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
