package com.appointmentProject.desktop.controller.pharmacy;

import com.appointmentProject.desktop.SceneNavigator;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PharmacyListController {

    @FXML
    private TableView<PharmacyRow> pharmacyTable;
    @FXML
    private TableColumn<PharmacyRow, String> addressCol;
    @FXML
    private TableColumn<PharmacyRow, String> phoneCol;
    @FXML
    private TableColumn<PharmacyRow, String> emailCol;

    @FXML
    private void initialize() {
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        loadPharmacies();
    }

    private void loadPharmacies() {
        ObservableList<PharmacyRow> list = FXCollections.observableArrayList();
        try {
            URL url = new URL("http://localhost:8080/pharmacy/all");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) sb.append(line);
            in.close();

            JsonArray arr = JsonParser.parseString(sb.toString()).getAsJsonArray();
            for (JsonElement el : arr) {
                JsonObject obj = el.getAsJsonObject();
                int id = obj.get("id").getAsInt();
                String address = obj.get("address").getAsString();
                String phone = obj.get("phone").getAsString();
                String email = obj.get("email").getAsString();
                list.add(new PharmacyRow(id, address, phone, email));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        pharmacyTable.setItems(list);
    }

    @FXML
    private void handleEdit() {
        PharmacyRow selected = pharmacyTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            PharmacyEditController.selectedPharmacyId = selected.getId();
            SceneNavigator.switchTo("/fxml/pharmacy_edit.fxml");
        }
    }

    @FXML
    private void handleCreate() {
        SceneNavigator.switchTo("/fxml/pharmacy_create.fxml");
    }

    public static class PharmacyRow {
        private final int id;
        private final String address;
        private final String phone;
        private final String email;

        public PharmacyRow(int id, String address, String phone, String email) {
            this.id = id;
            this.address = address;
            this.phone = phone;
            this.email = email;
        }

        public int getId() { return id; }
        public String getAddress() { return address; }
        public String getPhone() { return phone; }
        public String getEmail() { return email; }
    }
}
