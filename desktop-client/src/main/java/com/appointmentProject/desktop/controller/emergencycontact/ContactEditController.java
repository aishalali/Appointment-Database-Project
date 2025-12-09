package com.appointmentProject.desktop.controller.emergencycontact;

import com.appointmentProject.desktop.SceneNavigator;
import com.appointmentProject.desktop.SessionData;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ContactEditController {

    private int contactId;

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;

    @FXML private Label firstNameError;
    @FXML private Label lastNameError;
    @FXML private Label phoneError;
    @FXML private Label emailError;
    @FXML private Label addressError;

    @FXML private Button deleteButton;

    // Initialization
    @FXML
    private void initialize() {
        deleteButton.setVisible("ADMIN".equals(SessionData.currentUserRole));
    }

    // Load record data
    public void loadContact(int id, String first, String last, String phone, String email, String address) {
        this.contactId = id;

        // Always fill what we already know
        firstNameField.setText(first);
        lastNameField.setText(last);
        phoneField.setText(phone);

        // Fetch missing data from backend
        try {
            URL url = new URL("http://localhost:8080/emergencycontact/" + id);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String json = in.readLine();
            in.close();

            com.google.gson.JsonObject obj =
                    com.google.gson.JsonParser.parseString(json).getAsJsonObject();

            emailField.setText(obj.get("email").isJsonNull() ? "" : obj.get("email").getAsString());
            addressField.setText(obj.get("address").isJsonNull() ? "" : obj.get("address").getAsString());

        } catch (Exception e) {
            System.out.println("Failed to load extra contact data: " + e.getMessage());
        }
    }

    // Save Changes
    @FXML
    private void handleSave() {
        clearErrors();

        try {
            String json = "{"
                    + "\"id\":" + contactId + ","
                    + "\"firstName\":\"" + firstNameField.getText() + "\","
                    + "\"lastName\":\"" + lastNameField.getText() + "\","
                    + "\"phone\":\"" + phoneField.getText() + "\","
                    + "\"email\":" + toJson(emailField.getText()) + ","
                    + "\"address\":" + toJson(addressField.getText())
                    + "}";

            URL url = new URL("http://localhost:8080/emergencycontact/update");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            OutputStream os = con.getOutputStream();
            os.write(json.getBytes());
            os.close();

            if (con.getResponseCode() == 200) {
                ManageContactsController.successMessage = "Contact updated successfully.";
                SceneNavigator.switchTo("/fxml/manage_contacts.fxml");
            } else {
                showError("Update failed.");
            }

        } catch (Exception e) {
            showError("Update error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String toJson(String s) {
        if (s == null || s.isBlank()) return "null";
        return "\"" + s + "\"";
    }

    // Delete Contact
    @FXML
    private void handleDelete() {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete Contact");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Delete this emergency contact? This action cannot be undone.");

        if (alert.showAndWait().get() != ButtonType.OK) {
            return;
        }

        try {
            URL url = new URL("http://localhost:8080/emergencycontact/delete/" + contactId);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");

            if (con.getResponseCode() == 200) {
                ManageContactsController.successMessage = "Contact deleted successfully.";
                SceneNavigator.switchTo("/fxml/manage_contacts.fxml");
            } else {
                showError("Delete failed.");
            }

        } catch (Exception e) {
            showError("Delete error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        SceneNavigator.switchTo("/fxml/manage_contacts.fxml");
    }

    private void clearErrors() {
        firstNameError.setText("");
        lastNameError.setText("");
        phoneError.setText("");
        emailError.setText("");
        addressError.setText("");
    }

    private void showError(String msg) {
        Alert alert = new Alert(AlertType.ERROR, msg, ButtonType.OK);
        alert.show();
    }
}
