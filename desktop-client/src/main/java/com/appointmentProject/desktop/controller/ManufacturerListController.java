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

public class ManufacturerListController {

    @FXML private TableView<Row> table;
    @FXML private TableColumn<Row, Integer> idCol;
    @FXML private TableColumn<Row, String> nameCol;
    @FXML private TableColumn<Row, String> phoneCol;
    @FXML private TableColumn<Row, String> emailCol;
    @FXML private TableColumn<Row, String> addressCol;

    @FXML private TextField searchField;
    @FXML private Label messageLabel;

    @FXML private Button createButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private final ObservableList<Row> masterList = FXCollections.observableArrayList();

    public static class Row {
        private final int id;
        private final String name, phone, email, address;

        public Row(int id, String name, String phone, String email, String address) {
            this.id = id; this.name = name; this.phone = phone; this.email = email; this.address = address;
        }
        public int getId() { return id; }
        public String getName() { return name; }
        public String getPhone() { return phone; }
        public String getEmail() { return email; }
        public String getAddress() { return address; }
    }

    @FXML
    private void initialize() {
        idCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()).asObject());
        nameCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getName()));
        phoneCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPhone()));
        emailCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEmail()));
        addressCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getAddress()));

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
            URL url = new URL("http://localhost:8080/manufacturer/all");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String response = in.lines().reduce("", (a, b) -> a + b);
            in.close();

            JsonArray arr = JsonParser.parseString(response).getAsJsonArray();

            masterList.clear();
            for (JsonElement e : arr) {
                JsonObject o = e.getAsJsonObject();
                masterList.add(new Row(
                        o.get("id").getAsInt(),
                        o.get("manufacturerName").getAsString(),
                        o.get("phone").getAsString(),
                        o.get("email").getAsString(),
                        o.get("address").getAsString()
                ));
            }

            table.setItems(masterList);

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Failed to load data");
        }
    }

    private void filter(String q) {
        if (q == null || q.isBlank()) {
            table.setItems(masterList);
            return;
        }

        String lower = q.toLowerCase();
        table.setItems(masterList.filtered(x ->
                x.getName().toLowerCase().contains(lower) ||
                        x.getEmail().toLowerCase().contains(lower)
        ));
    }

    @FXML private void handleCreate() { /* open create screen */ }
    @FXML private void handleEdit() { /* open edit screen */ }
    @FXML private void handleDelete() { /* delete logic */ }
    @FXML
    public void handleBack() {
        SceneNavigator.switchTo("/fxml/admin_dashboard.fxml");
    }

}
