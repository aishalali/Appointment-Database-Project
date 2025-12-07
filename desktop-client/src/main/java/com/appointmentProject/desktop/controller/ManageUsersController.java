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
 * @version 1.0
 ***********************************************************************/

package com.appointmentProject.desktop.controller;

import com.appointmentProject.desktop.SceneNavigator;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ManageUsersController {

    @FXML private TableView<UserRow> usersTable;
    @FXML private TableColumn<UserRow, String> usernameCol;
    @FXML private TableColumn<UserRow, String> emailCol;
    @FXML private TableColumn<UserRow, String> roleCol;
    @FXML private Label messageLabel;

    private final Gson gson = new Gson();

    @FXML
    private void initialize() {
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

        loadUsers();
    }

    // LOAD ALL USERS FROM BACKEND
    private void loadUsers() {
        try {
            URL url = new URL("http://localhost:8080/account/all");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            JsonArray arr = gson.fromJson(in.readLine(), JsonArray.class);
            in.close();

            ObservableList<UserRow> rows = FXCollections.observableArrayList();

            arr.forEach(elem -> {
                JsonObject obj = elem.getAsJsonObject();

                rows.add(new UserRow(
                        obj.get("username").getAsString(),
                        obj.get("email").getAsString(),
                        obj.get("userType").getAsString()
                ));
            });

            usersTable.setItems(rows);
            messageLabel.setText("");

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error loading users.");
        }
    }

    // EDIT BUTTON CLICKED
    @FXML
    public void handleEditUser() {
        UserRow selected = usersTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            messageLabel.setText("Select a user first.");
            return;
        }

        EditUserController.selectedUsername = selected.getUsername();
        SceneNavigator.switchTo("/fxml/edit_user.fxml");
    }

    // DELETE BUTTON CLICKED
    @FXML
    public void handleDeleteUser() {
        UserRow selected = usersTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            messageLabel.setText("Select a user first.");
            return;
        }

        if (selected.getUsername().equals("admin")) {
            messageLabel.setText("Admin account cannot be deleted.");
            return;
        }

        try {
            URL url = new URL("http://localhost:8080/account/delete/" + selected.getUsername());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");
            con.getInputStream().close();

            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("User deleted.");

            loadUsers();

        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Error deleting user.");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleCreateUser() {
        SceneNavigator.switchTo("/fxml/create_user.fxml");
    }

    @FXML
    public void handleBack() {
        SceneNavigator.switchTo("/fxml/admin_dashboard.fxml");
    }

    // TABLE ROW MODEL
    public static class UserRow {
        private final String username;
        private final String email;
        private final String role;

        public UserRow(String username, String email, String role) {
            this.username = username;
            this.email = email;
            this.role = role;
        }

        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getRole() { return role; }
    }
}
