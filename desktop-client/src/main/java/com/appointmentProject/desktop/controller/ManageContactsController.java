/********************************************************************
 *  ManageContactsController.java
 *
 *          This controller is responsible for providing a list of
 *          existing Emergency Contacts for admin and receptionist
 *          user types.
 *
 * @author Matthew Kiyono
 * @version 1.0
 * @since 12/8/2025
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

public class ManageContactsController {

    // Navigation tracking
    public static String previousPage = "/fxml/admin_dashboard.fxml";

    // UI fields
    @FXML private TableView<ContactRow> contactTable;
    @FXML private TableColumn<ContactRow, Integer> idCol;
    @FXML private TableColumn<ContactRow, String> firstNameCol;
    @FXML private TableColumn<ContactRow, String> lastNameCol;
    @FXML private TableColumn<ContactRow, String> phoneCol;

    @FXML private Label messageLabel;


    // Row model for TableView
    public static class ContactRow {
        private final int id;
        private final String firstName;
        private final String lastName;
        private final String phone;

        public ContactRow(int id, String firstName, String lastName, String phone) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.phone = phone;
        }

        public int getId() { return id; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getPhone() { return phone; }
    }


    // Initialization
    @FXML
    private void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

        loadContacts();
    }


    // Load data from backend
    private void loadContacts() {
        try {
            URL url = new URL("http://localhost:8080/emergencycontact/all");
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
            ObservableList<ContactRow> rows = FXCollections.observableArrayList();

            for (JsonElement el : arr) {
                JsonObject obj = el.getAsJsonObject();
                int id = obj.get("id").getAsInt();
                String first = obj.get("firstName").getAsString();
                String last = obj.get("lastName").getAsString();
                String phone = obj.get("phone").getAsString();

                rows.add(new ContactRow(id, first, last, phone));
            }

            contactTable.setItems(rows);
            messageLabel.setText("");

        } catch (Exception e) {
            messageLabel.setText("Error loading contacts.");
            e.printStackTrace();
        }
    }


    //Create Contact Button
    @FXML
    private void handleCreateContact(){
        SceneNavigator.switchTo("/fxml/contact_create.fxml");
    }

    // Back navigation
    @FXML
    private void handleBack() {
        SceneNavigator.switchTo(previousPage);
    }
}
