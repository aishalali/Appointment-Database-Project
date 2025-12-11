package com.appointmentProject.desktop.controller.pharmacy;

import com.appointmentProject.desktop.SceneNavigator;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PharmacyCreateController {

    @FXML
    private TextField addressField;
    @FXML
    private TextField openingTimeField; // HH:mm
    @FXML
    private TextField closingTimeField; // HH:mm
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;

    @FXML
    private Label addressError;
    @FXML
    private Label phoneError;
    @FXML
    private Label emailError;

    @FXML
    private void handleSave() {
        clearErrors();
        String address = addressField.getText().trim();
        String openingTime = openingTimeField.getText().trim();
        String closingTime = closingTimeField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();

        boolean valid = true;

        if (address.isEmpty()) {
            addressError.setText("Required");
            valid = false;
        }
        if (phone.isEmpty()) {
            phoneError.setText("Required");
            valid = false;
        }
        if (email.isEmpty()) {
            emailError.setText("Required");
            valid = false;
        }

        if (!valid) return;

        try {
            JsonObject json = new JsonObject();
            json.addProperty("address", address);
            json.addProperty("openingTime", openingTime.isEmpty() ? null : openingTime);
            json.addProperty("closingTime", closingTime.isEmpty() ? null : closingTime);
            json.addProperty("phone", phone);
            json.addProperty("email", email);

            URL url = new URL("http://localhost:8080/pharmacy/create");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            OutputStream os = con.getOutputStream();
            os.write(json.toString().getBytes());
            os.close();

            int status = con.getResponseCode();
            if (status == 200 || status == 201) {
                SceneNavigator.switchTo("/fxml/pharmacy_list.fxml");
            } else {
                addressError.setText("Create failed (HTTP " + status + ")");
            }
        } catch (Exception e) {
            addressError.setText("Error creating pharmacy.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        SceneNavigator.switchTo("/fxml/pharmacy_list.fxml");
    }

    private void clearErrors() {
        addressError.setText("");
        phoneError.setText("");
        emailError.setText("");
    }
}
