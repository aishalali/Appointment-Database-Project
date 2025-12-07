/********************************************************************
 *  NurseListController.java
 *
 *          This controller is responsible for providing a list of
 *          employed nurses.
 *
 * @author Matthew Kiyono
 * @version 1.0
 * @since 12/6/2025
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

public class NurseListController {

    // --- FXML fields -------------------------------------------------
    @FXML private TableView<NurseRow> nursesTable;
    @FXML private TableColumn<NurseRow, Integer> idCol;
    @FXML private TableColumn<NurseRow, String> firstNameCol;
    @FXML private TableColumn<NurseRow, String> lastNameCol;
    @FXML private TableColumn<NurseRow, String> phoneCol;
    @FXML private TableColumn<NurseRow, String> emailCol;
    @FXML private TableColumn<NurseRow, String> addressCol;

    @FXML private Label messageLabel;

    // --- Inner row model ---------------------------------------------
    public static class NurseRow {
        private final int id;
        private final String firstName;
        private final String lastName;
        private final String phone;
        private final String email;
        private final String address;

        public NurseRow(int id,
                        String firstName,
                        String lastName,
                        String phone,
                        String email,
                        String address) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.phone = phone;
            this.email = email;
            this.address = address;
        }

        public int getId() { return id; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getPhone() { return phone; }
        public String getEmail() { return email; }
        public String getAddress() { return address; }
    }

    // --- Initialize --------------------------------------------------
    @FXML
    private void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        loadNurses();
    }

    // --- Load from backend -------------------------------------------
    private void loadNurses() {
        try {
            URL url = new URL("http://localhost:8080/nurse/all");
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

            ObservableList<NurseRow> rows = FXCollections.observableArrayList();

            for (JsonElement el : arr) {
                JsonObject obj = el.getAsJsonObject();
                int id = obj.get("id").getAsInt();
                String firstName = obj.get("firstName").getAsString();
                String lastName = obj.get("lastName").getAsString();
                String phone = obj.get("phone").getAsString();
                String email = obj.get("email").getAsString();
                String address = obj.get("address").getAsString();

                rows.add(new NurseRow(id, firstName, lastName, phone, email, address));
            }

            nursesTable.setItems(rows);
            messageLabel.setText("");

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error loading nurses.");
        }
    }

    // --- Button handlers ---------------------------------------------

    @FXML
    public void handleBack() {
        SceneNavigator.switchTo("/fxml/manage_staff.fxml");
    }

    @FXML
    public void handleCreateNurse() {
        messageLabel.setText("Create Nurse: not implemented yet.");
    }

    @FXML
    public void handleEditNurse() {
        NurseRow row = nursesTable.getSelectionModel().getSelectedItem();
        if (row == null) {
            messageLabel.setText("Please select a nurse to edit.");
            return;
        }

        messageLabel.setText("Edit Nurse: not implemented yet (selected ID: " + row.getId() + ")");
    }
}
