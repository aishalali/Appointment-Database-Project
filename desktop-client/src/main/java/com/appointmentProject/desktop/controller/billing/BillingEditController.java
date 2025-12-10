/********************************************************************
 *  BillingEditController.java
 *
 *          This controller is responsible for providing an admin
 *          user the ability to edit or delete an existing billing.
 *
 *
 * @author Jack Mitchell
 * @version 1.0
 * @since 12/9/2025
 ********************************************************************/

package com.appointmentProject.desktop.controller.billing;

import com.appointmentProject.desktop.SceneNavigator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class BillingEditController {

    public static int selectedBillingId;

    @FXML private TextField costField;
    @FXML private TextField statusOfPaymentField;
    @FXML private TextField paymentTypeField;
    @FXML private TextField insuranceIdField;

    @FXML private Label messageLabel;

    private final Gson gson = new Gson();

    @FXML
    private void initialize() {
        loadBillingData();
    }

    private void loadBillingData() {
        try {
            URL url = new URL("http://localhost:8080/billing/" + selectedBillingId);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            JsonObject obj = gson.fromJson(br.readLine(), JsonObject.class);
            br.close();

            costField.setText(obj.get("cost").getAsString());
            statusOfPaymentField.setText(obj.get("statusOfPayment").getAsString());
            paymentTypeField.setText(obj.get("paymentType").getAsString());
            insuranceIdField.setText(obj.get("insuranceId").getAsString());

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error loading billing.");
        }
    }

    @FXML
    private void handleSaveBilling() {
        try {
            JsonObject body = new JsonObject();
            body.addProperty("cost", costField.getText());
            body.addProperty("statusOfPayment", statusOfPaymentField.getText());
            body.addProperty("paymentType", paymentTypeField.getText());
            body.addProperty("insuranceId", insuranceIdField.getText());

            URL url = new URL("http://localhost:8080/billing/update/" + selectedBillingId);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");

            OutputStream os = con.getOutputStream();
            os.write(body.toString().getBytes());
            os.flush();
            os.close();

            if (con.getResponseCode() == 200) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Billing updated successfully!");
            } else {
                messageLabel.setText("Update failed.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Server error.");
        }
    }

    @FXML
    private void handleDeleteBilling() {
        try {
            URL url = new URL("http://localhost:8080/billing/delete/" + selectedBillingId);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");

            con.getInputStream().close();

            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Billing deleted successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error deleting billing.");
        }
    }

    @FXML
    private void handleBack() {
        SceneNavigator.switchTo("/fxml/billing_list.fxml");
    }
}
