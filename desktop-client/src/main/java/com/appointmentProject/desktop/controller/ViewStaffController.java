/*************************************************************************
 *  ViewStaffController.java
 *
 *      This controller is responsible for allowing user types Receptionist,
 *      Nurse, and Provider to view the First Name, Last Name, Phone Number,
 *      Email, and Specialty of every staff member (Nurses or Providers). If
 *      the staff member is a Nurse, their specialty will say "Nurse".
 *
 * @author Matthew Kiyono
 * @version 2.0
 * @since 12/7/2025
 **************************************************************************/
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

public class ViewStaffController {

    @FXML private TableView<StaffRow> staffTable;
    @FXML private TableColumn<StaffRow, String> firstNameCol;
    @FXML private TableColumn<StaffRow, String> lastNameCol;
    @FXML private TableColumn<StaffRow, String> phoneCol;
    @FXML private TableColumn<StaffRow, String> emailCol;
    @FXML private TableColumn<StaffRow, String> specialtyCol;

    @FXML private Label messageLabel;
    @FXML private TextField searchField;

    public static String previousPage = "/fxml/login.fxml";

    private final ObservableList<StaffRow> masterList = FXCollections.observableArrayList();

    // Row model
    public static class StaffRow {
        private final String firstName;
        private final String lastName;
        private final String phone;
        private final String email;
        private final String specialty;

        public StaffRow(String firstName, String lastName, String phone, String email, String specialty) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.phone = phone;
            this.email = email;
            this.specialty = specialty;
        }

        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getPhone() { return phone; }
        public String getEmail() { return email; }
        public String getSpecialty() { return specialty; }
    }

    @FXML
    private void initialize() {

        // Bind columns
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        specialtyCol.setCellValueFactory(new PropertyValueFactory<>("specialty"));

        applyColumnStyling();
        loadStaff();

        // Live search
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterStaff(newVal));
    }

    private void applyColumnStyling() {
        staffTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        leftAlign(firstNameCol);
        leftAlign(lastNameCol);
        leftAlign(emailCol);

        centerAlign(phoneCol);
        centerAlign(specialtyCol);

        firstNameCol.setMinWidth(140);
        lastNameCol.setMinWidth(140);
        phoneCol.setMinWidth(120);
        emailCol.setMinWidth(200);
        specialtyCol.setMinWidth(120);
    }

    private <T> void centerAlign(TableColumn<T, ?> col) {
        col.setStyle("-fx-alignment: CENTER;");
    }

    private <T> void leftAlign(TableColumn<T, ?> col) {
        col.setStyle("-fx-alignment: CENTER-LEFT;");
    }

    private void loadStaff() {
        try {
            URL url = new URL("http://localhost:8080/staff-view/all");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();

            JsonArray arr = com.google.gson.JsonParser.parseString(sb.toString()).getAsJsonArray();
            ObservableList<StaffRow> rows = FXCollections.observableArrayList();

            for (JsonElement el : arr) {
                JsonObject obj = el.getAsJsonObject();

                String firstName = obj.get("firstName").getAsString();
                String lastName = obj.get("lastName").getAsString();
                String phone = obj.get("phone").getAsString();
                String email = obj.get("email").getAsString();
                String specialty = obj.get("specialty").getAsString();

                rows.add(new StaffRow(firstName, lastName, phone, email, specialty));
            }

            masterList.setAll(rows);
            staffTable.setItems(masterList);
            messageLabel.setText("");

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error loading staff.");
        }
    }

    private void filterStaff(String query) {
        if (query == null || query.isBlank()) {
            staffTable.setItems(masterList);
            return;
        }

        String lower = query.toLowerCase();

        ObservableList<StaffRow> filtered = masterList.filtered(s ->
                s.getFirstName().toLowerCase().contains(lower) ||
                        s.getLastName().toLowerCase().contains(lower) ||
                        s.getPhone().toLowerCase().contains(lower) ||
                        s.getEmail().toLowerCase().contains(lower) ||
                        s.getSpecialty().toLowerCase().contains(lower)
        );

        staffTable.setItems(filtered);
    }

    @FXML
    private void handleBack() {
        SceneNavigator.switchTo(previousPage);
    }
}
