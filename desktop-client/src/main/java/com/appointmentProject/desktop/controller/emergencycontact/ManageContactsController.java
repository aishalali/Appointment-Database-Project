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
package com.appointmentProject.desktop.controller.emergencycontact;

import com.appointmentProject.desktop.SceneNavigator;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ManageContactsController {

    public static String previousPage = "/fxml/admin_dashboard.fxml";
    public static String successMessage = "";

    @FXML private TableView<ContactRow> contactTable;
    @FXML private TableColumn<ContactRow, Integer> idCol;
    @FXML private TableColumn<ContactRow, String> firstNameCol;
    @FXML private TableColumn<ContactRow, String> lastNameCol;
    @FXML private TableColumn<ContactRow, String> phoneCol;

    @FXML private Label messageLabel;

    // Row model
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
        idCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("id"));
        firstNameCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("lastName"));
        phoneCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("phone"));

        if (!successMessage.isEmpty()) {
            messageLabel.setText(successMessage);
            successMessage = "";
        }

        loadContacts();
    }

    // Load contacts
    private void loadContacts() {
        try {
            URL url = new URL("http://localhost:8080/emergencycontact/all");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String json = in.readLine();
            in.close();

            JsonArray arr = com.google.gson.JsonParser.parseString(json).getAsJsonArray();
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

        } catch (Exception e) {
            messageLabel.setText("Error loading contacts.");
            e.printStackTrace();
        }
    }

    // Create contact page
    @FXML
    private void handleCreateContact() {
        SceneNavigator.switchTo("/fxml/contact_create.fxml");
    }

    // Edit contact page
    @FXML
    private void handleEditContact() {
        ContactRow selected = contactTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/contact_edit.fxml"));
            Parent root = loader.load();

            ContactEditController controller = loader.getController();
            controller.loadContact(
                    selected.getId(),
                    selected.getFirstName(),
                    selected.getLastName(),
                    selected.getPhone(),
                    null,
                    null
            );

            SceneNavigator.setScene(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
        SceneNavigator.switchTo(previousPage);
    }
}

