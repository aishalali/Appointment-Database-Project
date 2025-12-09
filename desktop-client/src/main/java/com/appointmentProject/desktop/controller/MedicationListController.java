package com.appointmentProject.desktop.controller;

import com.appointmentProject.desktop.SceneNavigator;
import com.google.gson.*;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MedicationListController {

    @FXML private TableView<Row> table;
    @FXML private TableColumn<Row, Integer> idCol;
    @FXML private TableColumn<Row, String> nameCol;
    @FXML private TableColumn<Row, String> strengthCol;
    @FXML private TableColumn<Row, String> typeCol;
    @FXML private TableColumn<Row, String> consumptionCol;

    @FXML private TextField searchField;
    @FXML private Label messageLabel;

    @FXML private Button createButton, editButton, deleteButton;

    private final ObservableList<Row> masterList = FXCollections.observableArrayList();

    public static class Row {
        private final int id;
        private final String name, strength, type, consumption;

        public Row(int id, String name, String strength, String type, String consumption) {
            this.id = id; this.name = name; this.strength = strength;
            this.type = type; this.consumption = consumption;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getStrength() { return strength; }
        public String getType() { return type; }
        public String getConsumption() { return consumption; }
    }

    @FXML
    private void initialize() {
        idCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()).asObject());
        nameCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getName()));
        strengthCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getStrength()));
        typeCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getType()));
        consumptionCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getConsumption()));

        loadData();
        searchField.textProperty().addListener((o, oldV, newV) -> filter(newV));
        applyAdminPermissions();
    }

    private void applyAdminPermissions() {
        boolean isAdmin = UserSession.isAdmin();
        createButton.setDisable(!isAdmin);
        editButton.setDisable(!isAdmin);
        deleteButton.setDisable(!isAdmin);
    }

    private void loadData() {
        try {
            URL url = new URL("http://localhost:8080/medication/all");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String response = br.lines().reduce("", (a, b) -> a + b);
            br.close();

            JsonArray arr = JsonParser.parseString(response).getAsJsonArray();

            masterList.clear();
            for (JsonElement e : arr) {
                JsonObject o = e.getAsJsonObject();
                masterList.add(new Row(
                        o.get("id").getAsInt(),
                        o.get("medName").getAsString(),
                        o.get("strength").getAsString(),
                        o.get("type").getAsString(),
                        o.get("consumption").getAsString()
                ));
            }

            table.setItems(masterList);

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Failed to load medication data");
        }
    }

    private void filter(String q) {
        if (q == null || q.isBlank()) {
            table.setItems(masterList);
            return;
        }

        String lower = q.toLowerCase();
        table.setItems(masterList.filtered(x ->
                x.getName().toLowerCase().contains(lower)
        ));
    }

    @FXML private void handleCreate() { /* open create form */ }
    @FXML private void handleEdit() { /* open edit form */ }
    @FXML private void handleDelete() { /* delete logic */ }
    @FXML
    public void handleBack() {
        SceneNavigator.switchTo("/fxml/admin_dashboard.fxml");
    }

}

}
