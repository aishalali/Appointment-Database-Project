

package com.appointmentProject.desktop.controller.emergencycontact;

import com.appointmentProject.desktop.SceneNavigator;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ContactEditController {

    // Shared ID passed from ManageContactsController
    public static int selectedContactId;

    // UI fields
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;

    @FXML private Label messageLabel;


    // Initialization
    @FXML
    private void initialize() {
        loadContactDetails();
    }


    // Load existing contact into form fields
    private void loadContactDetails() {
        try {
            URL url = new URL("http://localhost:8080/emergencycontact/" + selectedContactId);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(con.getInputStream())
            );

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            reader.close();

            JsonObject obj = com.google.gson.JsonParser.parseString(sb.toString()).getAsJsonObject();

            firstNameField.setText(obj.get("firstName").getAsString());
            lastNameField.setText(obj.get("lastName").getAsString());
            phoneField.setText(obj.get("phone").getAsString());

            if (obj.has("email") && !obj.get("email").isJsonNull())
                emailField.setText(obj.get("email").getAsString());
            else
                emailField.setText("");

            if (obj.has("address") && !obj.get("address").isJsonNull())
                addressField.setText(obj.get("address").getAsString());
            else
                addressField.setText("");

            messageLabel.setText("");

        } catch (Exception e) {
            messageLabel.setText("Error loading contact.");
            e.printStackTrace();
        }
    }


    // Save changes (PUT request)
    @FXML
    private void handleSave() {
        try {
            URL url = new URL("http://localhost:8080/emergencycontact/update");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            JsonObject json = new JsonObject();
            json.addProperty("id", selectedContactId);
            json.addProperty("firstName", firstNameField.getText());
            json.addProperty("lastName", lastNameField.getText());
            json.addProperty("phone", phoneField.getText());
            json.addProperty("email", emailField.getText());
            json.addProperty("address", addressField.getText());

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(con.getOutputStream())
            );

            writer.write(json.toString());
            writer.flush();
            writer.close();

            int response = con.getResponseCode();

            if (response == 200) {
                messageLabel.setText("Contact updated successfully.");
            } else {
                messageLabel.setText("Update failed.");
            }

        } catch (Exception e) {
            messageLabel.setText("Error updating contact.");
            e.printStackTrace();
        }
    }


    // Back navigation
    @FXML
    private void handleBack() {
        SceneNavigator.switchTo("/fxml/manage_contacts.fxml");
    }
}
