/********************************************************************
 *  ContactCreateController.java
 *
 *          This controller is responsible for providing a "form" to
 *          admin and receptionist user types to create a new
 *          Emergency Contact in the database.
 *
 * @author Matthew Kiyono
 * @version 1.0
 * @since 12/8/2025
 ********************************************************************/
package com.appointmentProject.desktop.controller.emergencycontact;

import com.appointmentProject.desktop.SceneNavigator;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ContactCreateController {

    // Fields
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;

    // Error Labels
    @FXML private Label firstNameError;
    @FXML private Label lastNameError;
    @FXML private Label phoneError;
    @FXML private Label emailError;
    @FXML private Label addressError;

    // Initialization
    @FXML
    private void initialize() {
        clearErrors();
    }

    // Submit
    @FXML
    private void handleSubmit() {
        clearErrors();
        boolean valid = true;

        // Collect values
        String first = firstNameField.getText().trim();
        String last = lastNameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();

        // Validation
        if (first.isEmpty()) {
            firstNameError.setText("Required");
            valid = false;
        }

        if (last.isEmpty()) {
            lastNameError.setText("Required");
            valid = false;
        }

        if (phone.isEmpty()) {
            phoneError.setText("Required");
            valid = false;
        }

        if (!valid) return;

        // Create JSON body
        JsonObject json = new JsonObject();
        json.addProperty("firstName", first);
        json.addProperty("lastName", last);
        json.addProperty("phone", phone);
        json.addProperty("email", email.isEmpty() ? null : email);
        json.addProperty("address", address.isEmpty() ? null : address);

        try {
            URL url = new URL("http://localhost:8080/emergencycontact/add");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");

            OutputStream os = con.getOutputStream();
            os.write(json.toString().getBytes());
            os.flush();
            os.close();

            int code = con.getResponseCode();

            if (code == 200) {
                SceneNavigator.switchTo("/fxml/manage_contacts.fxml");
                return;
            }

            // If not 200, try reading error message
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            String msg = in.readLine();
            in.close();

            lastNameError.setText("Contact may already exist.");

        } catch (Exception e) {
            lastNameError.setText("Error creating contact.");
            e.printStackTrace();
        }
    }

    // Back
    @FXML
    private void handleBack() {
        SceneNavigator.switchTo("/fxml/manage_contacts.fxml");
    }

    // Clear errors
    private void clearErrors() {
        firstNameError.setText("");
        lastNameError.setText("");
        phoneError.setText("");
        emailError.setText("");
        addressError.setText("");
    }
}
