package com.appointmentProject.desktop.controller.pharmacy;

import com.appointmentProject.desktop.SceneNavigator;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PharmacyEditController {

    @FXML
    private TextField addressField;
    @FXML
    private TextField openingTimeField;
    @FXML
    private TextField closingTimeField;
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

    public static int selectedPharmacyId;

    @FXML
    private void initialize() {
        loadPharmacyDetails();
    }

    private void loadPharmacyDetails() {
        try {
            URL url = new URL("http://localhost:8080/pharmacy/" + selectedPharmacyId);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) sb.append(line);
            in.close();

            JsonObject obj = JsonParser.parseString(sb.toString()).getAsJsonObject();
            addressField.setText(obj.get("address").getAsString());
            openingTimeField.setText(obj.get("openingTime").isJsonNull() ? "" : obj.get("openingTime").getAsString());
            closingTimeField.setText(obj.get("closingTime").isJsonNull() ? "" : obj.get("closingTime").getAsString());
            phoneField.setText(obj.get("phone").getAsString());
            emailField.setText(obj.get("email").getAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdate() {
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
            json.addProperty("id", selectedPharmacyId);
            json.addProperty("address", address);
            json.addProperty("openingTime", openingTime.isEmpty() ? null : openingTime);
            json.addProperty("closingTime", closingTime.isEmpty() ? null : closingTime);
            json.addProperty("phone", phone);
            json.addProperty("email", email);

            URL url = new URL("http://localhost:8080/pharmacy/update");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json");
            OutputStream os = con.getOutputStream();
            os.write(json.toString().getBytes());
            os.close();

            int status = con.getResponseCode();
            if (status == 200) {
                SceneNavigator.switchTo("/fxml/pharmacy_list.fxml");
            } else {
                addressError.setText("Update failed (HTTP " + status + ")");
            }
        } catch (Exception e) {
            addressError.setText("Error updating pharmacy.");
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
