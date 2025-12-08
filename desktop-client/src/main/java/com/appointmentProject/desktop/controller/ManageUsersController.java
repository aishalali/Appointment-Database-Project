/*************************************************************************
 *  ManageUserController.java
 *
 *      The page that displays every user account in the database. A
 *      select* query is called to gather them all and the list only
 *      displays the username, email, and account type (no password for
 *      security reasons).
 *
 * @author Matthew Kiyono
 * @since 12/6/2025
 * @version 1.1
 ***********************************************************************/
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

public class ManageUsersController {


    @FXML private TableView<UserRow> usersTable;
    @FXML private TableColumn<UserRow, String> usernameCol;
    @FXML private TableColumn<UserRow, String> emailCol;
    @FXML private TableColumn<UserRow, String> userTypeCol;

    @FXML private Label messageLabel;
    @FXML private TextField searchField;

    private ObservableList<UserRow> masterList = FXCollections.observableArrayList();

    // Row Model
    public static class UserRow {
        private final String username;
        private final String email;
        private final String userType;

        public UserRow(String username, String email, String userType) {
            this.username = username;
            this.email = email;
            this.userType = userType;
        }

        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getUserType() { return userType; }
    }

    @FXML
    private void initialize() {
        // Bind columns
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        userTypeCol.setCellValueFactory(new PropertyValueFactory<>("userType"));

        applyColumnStyling();
        loadUsers();

        searchField.textProperty().addListener(
                (obs, oldVal, newVal) -> filterUsers(newVal)
        );
    }

    private void applyColumnStyling() {
        usersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        usernameCol.setMinWidth(140);
        emailCol.setMinWidth(200);
        userTypeCol.setMinWidth(120);

        leftAlign(usernameCol);
        leftAlign(emailCol);
        centerAlign(userTypeCol);
    }

    private <T> void leftAlign(TableColumn<T, ?> col) {
        col.setStyle("-fx-alignment: CENTER-LEFT;");
    }

    private <T> void centerAlign(TableColumn<T, ?> col) {
        col.setStyle("-fx-alignment: CENTER;");
    }

    private void loadUsers() {
        try {
            URL url = new URL("http://localhost:8080/account/all");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null)
                sb.append(line);
            in.close();

            JsonArray arr = com.google.gson.JsonParser.parseString(sb.toString()).getAsJsonArray();

            ObservableList<UserRow> rows = FXCollections.observableArrayList();

            for (JsonElement el : arr) {
                JsonObject obj = el.getAsJsonObject();

                String username = obj.get("username").getAsString();
                String email = obj.get("email").getAsString();
                String userType = obj.get("userType").getAsString();

                rows.add(new UserRow(username, email, userType));
            }

            masterList = rows;
            usersTable.setItems(masterList);

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error loading users.");
        }
    }

    private void filterUsers(String query) {
        if (query == null || query.isBlank()) {
            usersTable.setItems(masterList);
            return;
        }

        String lower = query.toLowerCase();

        ObservableList<UserRow> filtered = masterList.filtered(u ->
                u.getUsername().toLowerCase().contains(lower) ||
                        u.getEmail().toLowerCase().contains(lower) ||
                        u.getUserType().toLowerCase().contains(lower)
        );

        usersTable.setItems(filtered);
    }

    @FXML
    private void handleBack() {
        SceneNavigator.switchTo("/fxml/admin_dashboard.fxml");
    }

    @FXML
    private void handleCreateUser() {
        SceneNavigator.switchTo("/fxml/user_create.fxml");
    }

    @FXML
    private void handleEditUser() {
        UserRow row = usersTable.getSelectionModel().getSelectedItem();
        if (row == null) {
            messageLabel.setText("Please select a user to edit.");
            return;
        }

        // Set selection AND navigate
        EditUserController.selectedUsername = row.getUsername();
        SceneNavigator.switchTo("/fxml/user_edit.fxml");
    }
}
