/*************************************************************************
 *  ProviderListController.java
 *
 *      This controller is responsible for creating a roster of employed
 *      providers.
 *
 * @author Matthew Kiyono
 * @version 2.0
 * @since 12/6/2025
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

public class ProviderListController {

    @FXML private TableView<ProviderRow> providersTable;
    @FXML private TableColumn<ProviderRow, Integer> idCol;
    @FXML private TableColumn<ProviderRow, String> firstNameCol;
    @FXML private TableColumn<ProviderRow, String> lastNameCol;
    @FXML private TableColumn<ProviderRow, String> phoneCol;
    @FXML private TableColumn<ProviderRow, String> emailCol;
    @FXML private TableColumn<ProviderRow, String> specialtyCol;
    @FXML private TableColumn<ProviderRow, String> addressCol;

    @FXML private Label messageLabel;
    @FXML private TextField searchField;

    private final ObservableList<ProviderRow> masterList = FXCollections.observableArrayList();

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

        public int getId() { return id; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getPhone() { return phone; }
        public String getEmail() { return email; }
        public String getSpecialty() { return specialty; }
        public String getAddress() { return address; }
    }

    @FXML
    private void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        specialtyCol.setCellValueFactory(new PropertyValueFactory<>("specialty"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        applyColumnStyling();
        loadProviders();

        if (searchField != null) {
            searchField.textProperty().addListener((obs, oldVal, newVal) -> filterProviders(newVal));
        }
    }

    private void applyColumnStyling() {
        providersTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
        );

        centerAlign(idCol);
        centerAlign(phoneCol);
        centerAlign(specialtyCol);

        leftAlign(firstNameCol);
        leftAlign(lastNameCol);
        leftAlign(emailCol);
        leftAlign(addressCol);

        idCol.setMinWidth(60);
        firstNameCol.setMinWidth(140);
        lastNameCol.setMinWidth(140);
        phoneCol.setMinWidth(130);
        emailCol.setMinWidth(200);
        specialtyCol.setMinWidth(130);
        addressCol.setMinWidth(200);
    }

    private <T> void centerAlign(TableColumn<T, ?> col) {
        col.setStyle("-fx-alignment: CENTER;");
    }

    private <T> void leftAlign(TableColumn<T, ?> col) {
        col.setStyle("-fx-alignment: CENTER-LEFT;");
    }

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

            JsonArray arr = com.google.gson.JsonParser.parseString(sb.toString()).getAsJsonArray();

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

            masterList.setAll(rows);
            providersTable.setItems(masterList);
            messageLabel.setText("");

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error loading providers.");
        }
    }

    private void filterProviders(String query) {
        if (query == null || query.isBlank()) {
            providersTable.setItems(masterList);
            return;
        }

        String lower = query.toLowerCase();

        ObservableList<ProviderRow> filtered = masterList.filtered(p ->
                p.getFirstName().toLowerCase().contains(lower) ||
                        p.getLastName().toLowerCase().contains(lower) ||
                        p.getPhone().toLowerCase().contains(lower) ||
                        p.getEmail().toLowerCase().contains(lower) ||
                        p.getSpecialty().toLowerCase().contains(lower) ||
                        p.getAddress().toLowerCase().contains(lower) ||
                        String.valueOf(p.getId()).contains(lower)
        );

        providersTable.setItems(filtered);
    }

    @FXML
    public void handleBack() {
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

        ProviderEditController.selectedProviderId = row.getId();
        SceneNavigator.switchTo("/fxml/provider_edit.fxml");
    }
}
