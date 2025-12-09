/*************************************************************************
 *  ProviderEditController.java
 *
 *      This controller is responsible for providing the admin user a
 *      "form" to fill out to edit a Provider's information.
 *
 * @author Matthew Kiyono
 * @version 1.1
 * @since 12/6/2025
 **************************************************************************/
package com.appointmentProject.desktop.controller.provider;

import com.appointmentProject.desktop.SceneNavigator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProviderEditController {

    public static int selectedProviderId;   // Set by ProviderListController
    private final Gson gson = new Gson();

    @FXML private Label messageLabel;

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField specialtyField;
    @FXML private TextField addressField;

    @FXML
    private void initialize() {
        loadProvider();
    }


    // LOAD PROVIDER FROM BACKEND
    private void loadProvider() {
        try {
            URL url = new URL("http://localhost:8080/provider/" + selectedProviderId);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream())
            );
            String json = in.readLine();
            in.close();

            JsonObject obj = gson.fromJson(json, JsonObject.class);

            firstNameField.setText(obj.get("firstName").getAsString());
            lastNameField.setText(obj.get("lastName").getAsString());
            phoneField.setText(obj.get("phone").getAsString());
            emailField.setText(obj.get("email").getAsString());
            specialtyField.setText(obj.get("specialty").getAsString());
            addressField.setText(obj.get("address").getAsString());

            messageLabel.setText("");

        } catch (Exception e) {
            messageLabel.setText("Error loading provider.");
            e.printStackTrace();
        }
    }


    // SAVE CHANGES
    @FXML
    public void handleSaveProvider() {
        try {
            JsonObject body = new JsonObject();
            body.addProperty("firstName", firstNameField.getText().trim());
            body.addProperty("lastName", lastNameField.getText().trim());
            body.addProperty("phone", phoneField.getText().trim());
            body.addProperty("email", emailField.getText().trim());
            body.addProperty("specialty", specialtyField.getText().trim());
            body.addProperty("address", addressField.getText().trim());

            URL url = new URL("http://localhost:8080/provider/update/" + selectedProviderId);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            OutputStream os = con.getOutputStream();
            os.write(body.toString().getBytes());
            os.flush();
            os.close();

            int status = con.getResponseCode();

            if (status == 200) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Provider updated successfully!");
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Update failed.");
            }

        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Server error while updating.");
            e.printStackTrace();
        }
    }

    // DELETE PROVIDER
    @FXML
    public void handleDeleteProvider() {
        try {
            URL url = new URL("http://localhost:8080/provider/delete/" + selectedProviderId);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");

            con.getInputStream().close();

            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Provider deleted.");

        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Error deleting provider.");
            e.printStackTrace();
        }
    }


    // BACK
    @FXML
    public void handleBack() {
        SceneNavigator.switchTo("/fxml/provider_list.fxml");
    }
}
