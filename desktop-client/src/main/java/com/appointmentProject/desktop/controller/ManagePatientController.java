/********************************************************************
 *  PatientMenuController.java
 *
 *          This controller is responsible for providing a list of
 *          certain features (adding, removing, updating, and viewing)
 *          patients depending on the user type.
 *
 *
 * @author Matthew Kiyono
 * @version 1.0
 * @since 12/7/2025
 ********************************************************************/
package com.appointmentProject.desktop.controller;

import com.appointmentProject.desktop.SceneNavigator;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;

public class ManagePatientController {

    // --- Navigation tracking  --------------
    public static String previousPage = "/fxml/login.fxml";

    // --- FXML table fields ---------------------------------------------
    @FXML private TableView<PatientRow> patientTable;
    @FXML private TableColumn<PatientRow, Integer> idCol;
    @FXML private TableColumn<PatientRow, String> firstNameCol;
    @FXML private TableColumn<PatientRow, String> lastNameCol;
    @FXML private TableColumn<PatientRow, String> dobCol;
    @FXML private TableColumn<PatientRow, String> emailCol;
    @FXML private TableColumn<PatientRow, String> phoneCol;

    @FXML private Label messageLabel;

    // --- Inner row model (MUST match JSON fields) ----------------------
    public static class PatientRow {
        private final int id;
        private final String firstName;
        private final String lastName;
        private final String dob;     // stored as String for TableView convenience
        private final String email;
        private final String phone;

        public PatientRow(int id,
                          String firstName,
                          String lastName,
                          String dob,
                          String email,
                          String phone) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.dob = dob;
            this.email = email;
            this.phone = phone;
        }

        public int getId() { return id; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getDob() { return dob; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
    }

    // --- Initialize ----------------------------------------------------
    @FXML
    private void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        dobCol.setCellValueFactory(new PropertyValueFactory<>("dob"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

        loadPatients();
    }

    // --- Load Patients From Backend -----------------------------------
    private void loadPatients() {
        try {
            URL url = new URL("http://localhost:8080/patient/all");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();

            String json = sb.toString();
            JsonArray arr = com.google.gson.JsonParser.parseString(json).getAsJsonArray();

            ObservableList<PatientRow> rows = FXCollections.observableArrayList();

            for (JsonElement el : arr) {
                JsonObject obj = el.getAsJsonObject();

                int id = obj.get("id").getAsInt();
                String firstName = obj.get("firstName").getAsString();
                String lastName = obj.get("lastName").getAsString();

                // DoB comes as ISO string "YYYY-MM-DD"
                String dob = obj.get("doB").getAsString();

                String email = obj.get("email").isJsonNull() ? "" : obj.get("email").getAsString();
                String phone = obj.get("phone").getAsString();

                rows.add(new PatientRow(id, firstName, lastName, dob, email, phone));
            }

            patientTable.setItems(rows);
            messageLabel.setText("");

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error loading patients.");
        }
    }

    // --- Button Handlers -----------------------------
    @FXML
    public void handleBack() {
        SceneNavigator.switchTo(previousPage);
    }
}
