/********************************************************************
 *  PatientEditController.java
 *
 *          This controller is responsible for providing a "form" for
 *          users to update the current Patient data.
 *
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;

public class PatientEditController {

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

    private Integer currentInsuranceId = null;
    private Integer currentEmergencyId = null;

    @FXML
    private void initialize() {
        loadPatientDetails();
        loadInsuranceDropdown();
        loadEmergencyDropdown();
    }

    private void loadPatientDetails() {
        try {
            int id = ManagePatientController.selectedPatientId;

            URL url = new URL("http://localhost:8080/patient/" + id);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) sb.append(line);
            in.close();

            JsonObject obj = JsonParser.parseString(sb.toString()).getAsJsonObject();

            firstNameField.setText(obj.get("firstName").getAsString());
            lastNameField.setText(obj.get("lastName").getAsString());

            String dobStr = obj.get("doB").getAsString();
            dobPicker.setValue(LocalDate.parse(dobStr));

            ageField.setText(String.valueOf(obj.get("age").getAsInt()));
            phoneField.setText(obj.get("phone").getAsString());
            emailField.setText(obj.get("email").isJsonNull() ? "" : obj.get("email").getAsString());
            genderField.setText(obj.get("gender").isJsonNull() ? "" : obj.get("gender").getAsString());
            heightField.setText(String.valueOf(obj.get("height").getAsDouble()));
            weightField.setText(String.valueOf(obj.get("weight").getAsDouble()));

            currentInsuranceId = obj.get("insuranceId").isJsonNull()
                    ? null : obj.get("insuranceId").getAsInt();
            currentEmergencyId = obj.get("emergencyContactId").isJsonNull()
                    ? null : obj.get("emergencyContactId").getAsInt();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadInsuranceDropdown() {
        insuranceDropdown.getItems().clear();
        insuranceDropdown.getItems().add("None");

        try {
            URL url = new URL("http://localhost:8080/insurance/all");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) sb.append(line);
            in.close();

            JsonArray arr = JsonParser.parseString(sb.toString()).getAsJsonArray();

            String selectValue = "None";

            for (JsonElement el : arr) {
                JsonObject obj = el.getAsJsonObject();
                int id = obj.get("id").getAsInt();
                String name = obj.get("insuranceName").getAsString();
                String display = id + " - " + name;
                insuranceDropdown.getItems().add(display);

                if (currentInsuranceId != null && currentInsuranceId == id) {
                    selectValue = display;
                }
            }

            insuranceDropdown.setValue(selectValue);

        } catch (Exception e) {
            insuranceDropdown.getItems().add("Error Loading");
        }
    }

    private void loadEmergencyDropdown() {
        emergencyDropdown.getItems().clear();
        emergencyDropdown.getItems().add("None");

        try {
            URL url = new URL("http://localhost:8080/emergencycontact/all");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) sb.append(line);
            in.close();

            JsonArray arr = JsonParser.parseString(sb.toString()).getAsJsonArray();

            String selectValue = "None";

            for (JsonElement el : arr) {
                JsonObject obj = el.getAsJsonObject();
                int id = obj.get("id").getAsInt();
                String name = obj.get("firstName").getAsString() + " " +
                        obj.get("lastName").getAsString();
                String display = id + " - " + name;
                emergencyDropdown.getItems().add(display);

                if (currentEmergencyId != null && currentEmergencyId == id) {
                    selectValue = display;
                }
            }

            emergencyDropdown.setValue(selectValue);

        } catch (Exception e) {
            emergencyDropdown.getItems().add("Error Loading");
        }
    }

    @FXML
    private void handleUpdate() {

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

        if (insuranceDropdown.getValue() != null &&
                !insuranceDropdown.getValue().equals("None") &&
                !insuranceDropdown.getValue().startsWith("Error")) {
            insuranceId = Integer.parseInt(insuranceDropdown.getValue().split(" ")[0]);
        }

        if (emergencyDropdown.getValue() != null &&
                !emergencyDropdown.getValue().equals("None") &&
                !emergencyDropdown.getValue().startsWith("Error")) {
            emergencyId = Integer.parseInt(emergencyDropdown.getValue().split(" ")[0]);
        }

        boolean valid = true;

        if (first.isEmpty()) { firstNameError.setText("Required"); valid = false; }
        if (last.isEmpty()) { lastNameError.setText("Required"); valid = false; }
        if (dob == null) { dobError.setText("Required"); valid = false; }

        if (age < 0) { ageError.setText("Must be ≥ 0"); valid = false; }
        if (weight <= 0) { weightError.setText("Must be > 0"); valid = false; }
        if (height <= 0) { heightError.setText("Must be > 0"); valid = false; }

        if (!valid) return;

        JsonObject json = new JsonObject();
        int id = ManagePatientController.selectedPatientId;

        json.addProperty("id", id);
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
            URL url = new URL("http://localhost:8080/patient/update");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json");

            OutputStream os = con.getOutputStream();
            os.write(json.toString().getBytes());
            os.close();

            int status = con.getResponseCode();
            if (status == 200) {
                SceneNavigator.switchTo("/fxml/patient_details.fxml");
            } else {
                firstNameError.setText("Update failed (HTTP " + status + ")");
            }

        } catch (Exception e) {
            lastNameError.setText("First+Last must be unique.");
        }
    }

    @FXML
    private void handleCancel() {
        SceneNavigator.switchTo("/fxml/patient_details.fxml");
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
}
