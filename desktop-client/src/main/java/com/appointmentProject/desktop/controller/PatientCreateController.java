/********************************************************************
 *  PatientCreateController.java
 *
 *          This controller is responsible for providing a "form" to
 *          admin and receptionist user types to add new Patients to
 *          the database.
 *
 * @author Matthew Kiyono
 * @version 1.0
 * @since 12/8/2025
 ********************************************************************/
package com.appointmentProject.desktop.controller;

import com.appointmentProject.desktop.SceneNavigator;
import com.google.gson.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;

public class PatientCreateController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private DatePicker dobPicker;
    @FXML private TextField ageField;
    @FXML private TextField weightField;
    @FXML private TextField heightField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField genderField;

    @FXML private ComboBox<String> insuranceDropdown;
    @FXML private ComboBox<String> emergencyDropdown;

    @FXML private Label firstNameError, lastNameError, dobError, ageError,
            weightError, heightError, phoneError, emailError, genderError,
            insuranceError, emergencyError;

    @FXML
    private void initialize() {
        loadInsuranceDropdown();
        loadEmergencyDropdown();
    }


    // Load dropdowns
    private void loadInsuranceDropdown() {
        insuranceDropdown.getItems().add("None");

        try {
            URL url = new URL("http://localhost:8080/insurance/all");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String json = in.readLine();
            in.close();

            JsonArray arr = JsonParser.parseString(json).getAsJsonArray();

            for (JsonElement el : arr) {
                JsonObject obj = el.getAsJsonObject();
                int id = obj.get("id").getAsInt();
                String name = obj.get("insuranceName").getAsString();
                insuranceDropdown.getItems().add(id + " - " + name);
            }

        } catch (Exception e) {
            insuranceDropdown.getItems().add("Error Loading");
        }
    }

    private void loadEmergencyDropdown() {
        emergencyDropdown.getItems().add("None");

        try {
            URL url = new URL("http://localhost:8080/emergencycontact/all");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String json = in.readLine();
            in.close();

            JsonArray arr = JsonParser.parseString(json).getAsJsonArray();

            for (JsonElement el : arr) {
                JsonObject obj = el.getAsJsonObject();
                int id = obj.get("id").getAsInt();
                String name = obj.get("firstName").getAsString() + " " +
                        obj.get("lastName").getAsString();
                emergencyDropdown.getItems().add(id + " - " + name);
            }

        } catch (Exception e) {
            emergencyDropdown.getItems().add("Error Loading");
        }
    }


    // Submit
    @FXML
    private void handleSubmit() {

        clearErrors();

        String first = firstNameField.getText().trim();
        String last = lastNameField.getText().trim();

        LocalDate dob = dobPicker.getValue();
        int age = parseInt(ageField.getText(), ageError);
        double weight = parseDouble(weightField.getText(), weightError);
        double height = parseDouble(heightField.getText(), heightError);
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String gender = genderField.getText().trim();

        Integer insuranceId = null;
        Integer emergencyId = null;

        if (!insuranceDropdown.getValue().equals("None"))
            insuranceId = Integer.parseInt(insuranceDropdown.getValue().split(" ")[0]);

        if (!emergencyDropdown.getValue().equals("None"))
            emergencyId = Integer.parseInt(emergencyDropdown.getValue().split(" ")[0]);

        boolean valid = true;

        // VALIDATION RULES
        if (first.isEmpty()) { firstNameError.setText("Required"); valid = false; }
        if (last.isEmpty()) { lastNameError.setText("Required"); valid = false; }
        if (dob == null) { dobError.setText("Required"); valid = false; }

        if (age < 0) { ageError.setText("Must be ≥ 0"); valid = false; }
        if (weight <= 0) { weightError.setText("Must be > 0"); valid = false; }
        if (height <= 0) { heightError.setText("Must be > 0"); valid = false; }

        if (!valid) return;

        // Create JSON
        JsonObject json = new JsonObject();
        json.addProperty("firstName", first);
        json.addProperty("lastName", last);
        json.addProperty("doB", dob.toString());
        json.addProperty("age", age);
        json.addProperty("weight", weight);
        json.addProperty("height", height);
        json.addProperty("phone", phone);
        json.addProperty("email", email.isEmpty() ? null : email);
        json.addProperty("gender", gender.isEmpty() ? null : gender);
        json.addProperty("insuranceId", insuranceId);
        json.addProperty("emergencyContactId", emergencyId);

        try {
            URL url = new URL("http://localhost:8080/patient/add");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");

            OutputStream os = con.getOutputStream();
            os.write(json.toString().getBytes());
            os.close();

            if (con.getResponseCode() == 200) {
                SceneNavigator.switchTo("/fxml/manage_patient.fxml");
            } else {
                firstNameError.setText("Patient may already exist.");
            }

        } catch (Exception e) {
            lastNameError.setText("First + Last must be unique.");
        }
    }

    private int parseInt(String s, Label errorLabel) {
        try { return Integer.parseInt(s); }
        catch (Exception e) { errorLabel.setText("Invalid"); return -1; }
    }

    private double parseDouble(String s, Label errorLabel) {
        try { return Double.parseDouble(s); }
        catch (Exception e) { errorLabel.setText("Invalid"); return -1; }
    }

    private void clearErrors() {
        firstNameError.setText("");
        lastNameError.setText("");
        dobError.setText("");
        ageError.setText("");
        weightError.setText("");
        heightError.setText("");
        phoneError.setText("");
        emailError.setText("");
        genderError.setText("");
        insuranceError.setText("");
        emergencyError.setText("");
    }

    @FXML
    private void handleBack() {
        SceneNavigator.switchTo("/fxml/manage_patient.fxml");
    }
}
