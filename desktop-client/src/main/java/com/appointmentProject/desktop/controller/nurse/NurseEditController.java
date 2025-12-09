/********************************************************************
 *  NurseEditController.java
 *
 *          This controller is responsible for providing an admin
 *          user the ability to edit or delete an existing nurse.
 *
 *
 * @author Matthew Kiyono
 * @version 1.0
 * @since 12/7/2025
 ********************************************************************/

package com.appointmentProject.desktop.controller.nurse;

import com.appointmentProject.desktop.SceneNavigator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class NurseEditController {

    public static int selectedNurseId;

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;

    @FXML private Label messageLabel;

    private final Gson gson = new Gson();

    @FXML
    private void initialize() {
        loadNurseData();
    }

    private void loadNurseData() {
        try {
            URL url = new URL("http://localhost:8080/nurse/" + selectedNurseId);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            JsonObject obj = gson.fromJson(br.readLine(), JsonObject.class);
            br.close();

            firstNameField.setText(obj.get("firstName").getAsString());
            lastNameField.setText(obj.get("lastName").getAsString());
            phoneField.setText(obj.get("phone").getAsString());
            emailField.setText(obj.get("email").getAsString());
            addressField.setText(obj.get("address").getAsString());

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error loading nurse.");
        }
    }

    @FXML
    private void handleSaveNurse() {
        try {
            JsonObject body = new JsonObject();
            body.addProperty("firstName", firstNameField.getText());
            body.addProperty("lastName", lastNameField.getText());
            body.addProperty("phone", phoneField.getText());
            body.addProperty("email", emailField.getText());
            body.addProperty("address", addressField.getText());

            URL url = new URL("http://localhost:8080/nurse/update/" + selectedNurseId);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");

            OutputStream os = con.getOutputStream();
            os.write(body.toString().getBytes());
            os.flush();
            os.close();

            if (con.getResponseCode() == 200) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Nurse updated successfully!");
            } else {
                messageLabel.setText("Update failed.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Server error.");
        }
    }

    @FXML
    private void handleDeleteNurse() {
        try {
            URL url = new URL("http://localhost:8080/nurse/delete/" + selectedNurseId);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");

            con.getInputStream().close();

            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Nurse deleted successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error deleting nurse.");
        }
    }

    @FXML
    private void handleBack() {
        SceneNavigator.switchTo("/fxml/nurse_list.fxml");
    }
}
