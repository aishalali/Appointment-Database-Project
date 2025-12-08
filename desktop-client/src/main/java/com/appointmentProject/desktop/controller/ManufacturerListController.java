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

public class ManufacturerListController {

    @FXML private TableView<ManufacturerRow> manufacturersTable;
    @FXML private TableColumn<ManufacturerRow, Integer> idCol;
    @FXML private TableColumn<ManufacturerRow, String> nameCol;
    @FXML private TableColumn<ManufacturerRow, String> phoneCol;
    @FXML private TableColumn<ManufacturerRow, String> emailCol;
    @FXML private TableColumn<ManufacturerRow, String> addressCol;

    @FXML private Label messageLabel;
    @FXML private TextField searchField;

    private final ObservableList<ManufacturerRow> masterList =
            FXCollections.observableArrayList();

    /**
     * Row wrapper for the table, similar to ProviderRow.
     */
    public static class ManufacturerRow {
        private final int id;
        private final String name;
        private final String phone;
        private final String email;
        private final String address;

        public ManufacturerRow(int id,
                               String name,
                               String phone,
                               String email,
                               String address) {
            this.id = id;
            this.name = name;
            this.phone = phone;
            this.email = email;
            this.address = address;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getPhone() { return phone; }
        public String getEmail() { return email; }
        public String getAddress() { return address; }
    }

    @FXML
    private void initialize() {
        // Bind columns to properties in ManufacturerRow
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        applyColumnStyling();
        loadManufacturers();

        if (searchField != null) {
            searchField.textProperty().addListener(
                    (obs, oldVal, newVal) -> filterManufacturers(newVal)
            );
        }
    }

    private void applyColumnStyling() {
        manufacturersTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
        );

        centerAlign(idCol);
        centerAlign(phoneCol);

        leftAlign(nameCol);
        leftAlign(emailCol);
        leftAlign(addressCol);

        idCol.setMinWidth(60);
        nameCol.setMinWidth(180);
        phoneCol.setMinWidth(130);
        emailCol.setMinWidth(200);
        addressCol.setMinWidth(200);
    }

    private <T> void centerAlign(TableColumn<T, ?> col) {
        col.setStyle("-fx-alignment: CENTER;");
    }

    private <T> void leftAlign(TableColumn<T, ?> col) {
        col.setStyle("-fx-alignment: CENTER-LEFT;");
    }

    /**
     * Loads manufacturers from the backend REST endpoint.
     * Expects JSON array with fields:
     *   id, manufacturerName, phone, email, address
     */
    private void loadManufacturers() {
        try {
            URL url = new URL("http://localhost:8080/manufacturer/all");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in =
                    new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();

            JsonArray arr = com.google.gson.JsonParser
                    .parseString(sb.toString())
                    .getAsJsonArray();

            ObservableList<ManufacturerRow> rows =
                    FXCollections.observableArrayList();

            for (JsonElement el : arr) {
                JsonObject obj = el.getAsJsonObject();

                int id = obj.get("id").getAsInt();
                String name = obj.get("manufacturerName").getAsString();
                String phone = obj.get("phone").getAsString();
                String email = obj.get("email").getAsString();
                String address = obj.get("address").getAsString();

                rows.add(new ManufacturerRow(id, name, phone, email, address));
            }

            masterList.setAll(rows);
            manufacturersTable.setItems(masterList);
            messageLabel.setText("");

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error loading manufacturers.");
        }
    }

    private void filterManufacturers(String query) {
        if (query == null || query.isBlank()) {
            manufacturersTable.setItems(masterList);
            return;
        }

        String lower = query.toLowerCase();

        ObservableList<ManufacturerRow> filtered = masterList.filtered(m ->
                m.getName().toLowerCase().contains(lower) ||
                        m.getPhone().toLowerCase().contains(lower) ||
                        m.getEmail().toLowerCase().contains(lower) ||
                        m.getAddress().toLowerCase().contains(lower) ||
                        String.valueOf(m.getId()).contains(lower)
        );

        manufacturersTable.setItems(filtered);
    }

    @FXML
    public void handleBack() {
        // Same screen Provider uses
        SceneNavigator.switchTo("/fxml/manage_staff.fxml");
    }

    @FXML
    public void handleCreateManufacturer() {
        // Hook this up later when you have a manufacturer_create.fxml
        messageLabel.setText("Create Manufacturer not implemented yet.");
    }

    @FXML
    public void handleEditManufacturer() {
        ManufacturerRow row = manufacturersTable.getSelectionModel().getSelectedItem();
        if (row == null) {
            messageLabel.setText("Please select a manufacturer to edit.");
            return;
        }

        // When you make an edit screen, you can pass the ID like the provider does:
        // ManufacturerEditController.selectedManufacturerId = row.getId();
        messageLabel.setText("Edit Manufacturer not implemented yet (ID: " + row.getId() + ").");
    }
}
