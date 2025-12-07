/*************************************************************************
 *  ProviderListController.java
 *
 *      This controller is responsible for creating a roster of employed
 *      providers.
 *
 * @author Matthew Kiyono
 * @version 1.0
 * @since 12/6/2025
 **************************************************************************/

package com.appointmentProject.desktop.controller;

import com.appointmentProject.desktop.SceneNavigator;
import com.google.gson.Gson;
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

public class ProviderListController {

    // --- FXML fields -------------------------------------------------
    @FXML private TableView<ProviderRow> providersTable;
    @FXML private TableColumn<ProviderRow, Integer> idCol;
    @FXML private TableColumn<ProviderRow, String> firstNameCol;
    @FXML private TableColumn<ProviderRow, String> lastNameCol;
    @FXML private TableColumn<ProviderRow, String> phoneCol;
    @FXML private TableColumn<ProviderRow, String> emailCol;
    @FXML private TableColumn<ProviderRow, String> specialtyCol;
    @FXML private TableColumn<ProviderRow, String> addressCol;

    @FXML private Label messageLabel;

    private final Gson gson = new Gson();

    // --- Inner row model for the TableView ---------------------------
    public static class ProviderRow {
        private final int id;
        private final String firstName;
        private final String lastName;
        private final String phone;
        private final String email;
        private final String specialty;
        private final String address;

        public ProviderRow(int id,
                           String firstName,
                           String lastName,
                           String phone,
                           String email,
                           String specialty,
                           String address) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.phone = phone;
            this.email = email;
            this.specialty = specialty;
            this.address = address;
        }

        // getters for PropertyValueFactory
        public int getId() { return id; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getPhone() { return phone; }
        public String getEmail() { return email; }
        public String getSpecialty() { return specialty; }
        public String getAddress() { return address; }
    }

    // --- Initialize --------------------------------------------------
    @FXML
    private void initialize() {
        // Hook up columns to ProviderRow getters
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        specialtyCol.setCellValueFactory(new PropertyValueFactory<>("specialty"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        loadProviders();
    }

    // --- Load data from backend -------------------------------------
    private void loadProviders() {
        try {
            URL url = new URL("http://localhost:8080/provider/all");
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
            // Expecting a JSON array of Provider objects
            JsonArray arr = com.google.gson.JsonParser.parseString(json).getAsJsonArray();

            ObservableList<ProviderRow> rows = FXCollections.observableArrayList();

            for (JsonElement el : arr) {
                JsonObject obj = el.getAsJsonObject();
                int id = obj.get("id").getAsInt();
                String firstName = obj.get("firstName").getAsString();
                String lastName = obj.get("lastName").getAsString();
                String phone = obj.get("phone").getAsString();
                String email = obj.get("email").getAsString();
                String specialty = obj.get("specialty").getAsString();
                String address = obj.get("address").getAsString();

                rows.add(new ProviderRow(id, firstName, lastName, phone, email, specialty, address));
            }

            providersTable.setItems(rows);
            messageLabel.setText(""); // clear any previous error

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error loading providers.");
        }
    }

    // --- Button handlers ---------------------------------------------

    @FXML
    public void handleBack() {
        // Back to Manage Staff screen
        SceneNavigator.switchTo("/fxml/manage_staff.fxml");
    }

    @FXML
    public void handleCreateProvider() {
        SceneNavigator.switchTo("/fxml/provider_create.fxml");
    }

    @FXML
    public void handleEditProvider() {
        ProviderRow row = providersTable.getSelectionModel().getSelectedItem();
        if (row == null) {
            messageLabel.setText("Please select a provider to edit.");
            return;
        }

        // TODO: Later we will:
        //  - store selected provider ID somewhere (like EditProviderController.selectedId)
        //  - SceneNavigator.switchTo("/fxml/edit_provider.fxml");
        messageLabel.setText("Edit Provider: not implemented yet (selected ID: " + row.getId() + ")");
    }
}
